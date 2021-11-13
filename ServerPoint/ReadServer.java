package com.yang.bioDPointObject.ServerPoint;

/**
 * @Author Chengzhi
 * @Date 2021/11/6 0:30
 * @Version 1.0
 */

import com.yang.bioDPointObject.Entry.Message;
import com.yang.bioDPointObject.Entry.MessageRedis;
import com.yang.bioDPointObject.ServerPoint.redis.WriteToRedis;
import com.yang.bioDPointObject.Util.BloomFileter.BloomFileter;
import com.yang.bioDPointObject.Util.close.CloseUtil;
import com.yang.bioDPointObject.Util.serialize.SerializeUtil;
import redis.clients.jedis.Jedis;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

/**
 * @Author Chengzhi
 * @Date 2021/11/3 11:11
 * @Version 1.0
 *
 * 服务器端使用
 *
 * 读取消息
 */
public class ReadServer implements Runnable {

    static Logger log = Logger.getLogger("ReadUtilServer");

    //输入缓冲流
    BufferedInputStream bufferedInputStream;
    //输出缓冲流
    BufferedOutputStream bufferedOutputStream;
    //对端Socket
    Socket socket;
    //已经连接的客户端的SocketList (全局使用)
    ConcurrentLinkedQueue<Socket> connectedSocketList;
    //使用一个HashMap 存储 <Socket,姓名>
    ConcurrentHashMap<Socket,String> clientNameMap;
    //使用一个HashMap 存储 <姓名,Socket> 方便根据接收者的名称找到对应的Socket
    ConcurrentHashMap<String,Socket> clientSocketMap;
    //使用一个HashMap 存储消息 <发送者名称,发送的消息> <senderName,MessageRedis>
    ConcurrentHashMap<String, Integer> msgMemoryMap;


    //序列化实例
    SerializeUtil serializeUtil;

    //返回响应
    ResponseToClient responseToClient;

    //redis 操作实例
    Jedis jedis;

    //传入写入Redis的线程
    WriteToRedis writeToRedis;

    public ReadServer(Socket socket, ConcurrentLinkedQueue<Socket> connectedSocketList, ConcurrentHashMap<Socket,String> clientNameMap, ConcurrentHashMap<String,Socket> clientSocketMap, ConcurrentHashMap<String, Integer> msgMemoryMap,WriteToRedis writeToRedis) throws IOException {
        this.bufferedInputStream = new BufferedInputStream(socket.getInputStream());
        this.bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
        this.serializeUtil = new SerializeUtil();
        this.socket = socket;
        this.connectedSocketList = connectedSocketList;
        this.clientNameMap = clientNameMap;
        this.clientSocketMap = clientSocketMap;
        this.msgMemoryMap = msgMemoryMap;
        this.responseToClient = new ResponseToClient(socket,clientNameMap,clientSocketMap);
        this.jedis = new Jedis("localhost");
        this.writeToRedis = writeToRedis;
    }

    //读取消息
    public void run() {
        byte[] bytes = new byte[1024];
        //通过socket，获取输入流;
        try{
            //创建布隆过滤器
            BloomFileter fileter = new BloomFileter(7);
            //创建消息队列
            CopyOnWriteArrayList<MessageRedis> messageRedisList = new CopyOnWriteArrayList<MessageRedis>( );
            //循环的读取对端发送的数据
                while (true) {

                    bufferedInputStream.read(bytes);

                    //将字节数组序列化为对象
                    Message accMsg = (Message) serializeUtil.byteArrayToObject(bytes);

                    //判断消息类型
                    //根据 code 来
                    if (accMsg.getCode() == -1) {//注册
                        System.out.println("Client Point " + socket.getInetAddress() + "\t请求注册：" + "注册用户名为：" + accMsg.getMsg());
                        //调用注册方法
                        responseToClient.Register(accMsg.getMsg());
                    }
                    if(accMsg.getCode() == -2){// -2 表示返回当前聊天室客户端名称列表
                        System.out.println("Client Point " + socket.getInetAddress() + "\t请求获取：" + "当前聊天室所有用户名称");
                        //返回当前用户列表
                        responseToClient.responseClientList(socket,clientNameMap);
                    }
                    if(accMsg.getCode() == 200){// 200 表示是发送给其他客户端的信息
                        if (!clientNameMap.containsValue(accMsg.getSendToName())) {//服务器端进行校验,如果接收者不存在
                            //返回错误信息
                            responseToClient.responseError(socket);
                        } else {
                            //判断消息是否重复，即消息是否存在于布隆过滤器中
                            //不存在重复则放入，返回false
                            //存在则直接返回 true
                            boolean isRepeat = fileter.addIfNotExist(String.valueOf(accMsg.getUuid()));
                            if (isRepeat){log.info("该消息是重复消息");continue;}
                            else {
                                String senderName = clientNameMap.get(socket);
                                /**
                                 * 因为在客户端做了超时重传，为了避免因为Ack丢失而引起的超时重传导致服务器端收到重复消息
                                 * 有以下三种方案：
                                 * 1.因为上一条消息已经存在redis中，每当消息来临的时候先判断是否存在于redis中，由于redis存储的是<橙汁,<消息一,消息二>
                                 *   使用redis取出很麻烦
                                 * 2.使用哈希表，创建一个哈希表来存放已经处理的消息，每当消息来先在哈希表中查找看是否存在，缺点，占用空间大
                                 * 3.布隆过滤器
                                 */
                                //写入 Redis
                                //转换 消息格式
                                MessageRedis messageRedis = new MessageRedis();
                                messageRedis.setMsg(accMsg.getMsg());
                                messageRedis.setSenderName(clientNameMap.get(socket));
                                messageRedis.setSendToName(accMsg.getSendToName());
                                messageRedis.setLength(accMsg.getLength());
                                messageRedis.setWriteTime(accMsg.getDate());
                                messageRedisList.add(messageRedis);
                                //发送反馈给客户端
                                if(save(senderName,messageRedisList)){
                                    //转发
                                    forward(clientSocketMap.get(accMsg.getSendToName()),messageRedis);
                                    //给客户端发送确认消息
                                    responseToClient.responseMsgStatus(socket,accMsg.getUuid());
                                }
                                //保存到内存
                                //save(accMsg,senderName);
                            }
                        }
                    }
            }
        }catch (Exception e) {
            connectedSocketList.remove(socket);
            log.info("读取消息时发现 客户端端口号：" + socket.getPort() + "\t连接已中断");
            e.printStackTrace();
        }
        finally {
            CloseUtil.close(socket);
        }
    }

//    /**
//     * 将消息写入 Redis
//     * @param accMsg 写入Redis的Msg
//     * @param senderName 消息发送者名称
//     */
//    public void save(Message accMsg,String senderName) {
//        MessageRedis messageRedis = new MessageRedis();
//        messageRedis.setMsg(accMsg.getMsg());
//        messageRedis.setSendToName(accMsg.getSendToName());
//        messageRedis.setLength(accMsg.getLength());
//        messageRedis.setWriteTime(accMsg.getDate());
//        writeToRedis.setKey(senderName);
//        writeToRedis.setMessageRedis(messageRedis);
//    }


    /**
     * 上面是存入单个消息
     * 下面是存入 <橙汁,<消息一，消息二...>
     * @param
     * @param senderName
     */
    public Boolean save(String senderName,CopyOnWriteArrayList<MessageRedis> messageRedisList){
        log.info("save方法成功调用...");
        writeToRedis.setKey(senderName);
        writeToRedis.setMessageList(messageRedisList);
        return true;
    }

    /**
     * 转发到对应客户端
     */
    public void forward(Socket socket,MessageRedis messageRedis) throws IOException {
        log.info("目标Socket为：" + socket);
        //创建输出流
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
        log.info("创建输出流成功！");
        //序列化
        byte[] bytes = serializeUtil.objectToByteArray(messageRedis);

        //发送消息
        try {
            bufferedOutputStream.write(bytes);
            bufferedOutputStream.flush();
            log.info("消息转发成功！");
        } catch (Exception e) {
            log.info("连接已中断!");
            e.printStackTrace();
        }
        log.info("响应完毕");
    }
}
