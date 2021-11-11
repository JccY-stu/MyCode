package com.yang.bioDPointObject.ServerPoint;

/**
 * @Author Chengzhi
 * @Date 2021/11/6 0:30
 * @Version 1.0
 */

import com.yang.bioDPointObject.Entry.Message;
import com.yang.bioDPointObject.Entry.MessageRedis;
import com.yang.bioDPointObject.ServerPoint.redis.WriteToRedis;
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
    //使用一个HashMap 存储消息 <发送者名称,发送的消息> <senderName,MessageRedis>
    ConcurrentHashMap<String, Integer> msgMemoryMap = new ConcurrentHashMap<String, Integer>();
    //使用一个List 存储消息 <发送的消息列表> <senderName,MessageRedis>
    List<MessageRedis> everySenderMsgList = new ArrayList<MessageRedis>();

    //序列化实例
    SerializeUtil serializeUtil;

    //返回响应
    ResponseToClient responseToClient;

    //redis 操作实例
    Jedis jedis;

    //传入写入Redis的线程
    WriteToRedis writeToRedis;

    public ReadServer(Socket socket, ConcurrentLinkedQueue<Socket> connectedSocketList, ConcurrentHashMap<Socket,String> clientNameMap, ConcurrentHashMap<String, Integer> msgMemoryMap,ArrayList everySenderMsgList,WriteToRedis writeToRedis) throws IOException {
        this.bufferedInputStream = new BufferedInputStream(socket.getInputStream());
        this.bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
        this.serializeUtil = new SerializeUtil();
        this.socket = socket;
        this.connectedSocketList = connectedSocketList;
        this.clientNameMap = clientNameMap;
        this.msgMemoryMap = msgMemoryMap;
        this.everySenderMsgList = everySenderMsgList;
        this.responseToClient = new ResponseToClient(socket,clientNameMap);
        this.jedis = new Jedis("localhost");
        this.writeToRedis = writeToRedis;
    }

    //读取消息
    public void run() {
        byte[] bytes = new byte[1024];
        //通过socket，获取输入流;
        try{
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
                        if (!clientNameMap.containsValue(accMsg.getSendToName())) {//服务器端进行校验,如果不存在
                            //返回错误信息
                            responseToClient.responseError(socket);
                        } else {
                            String senderName = clientNameMap.get(socket);
                            //如果存在，则存入 Broker 考虑通过 Redis 来实现
                            System.out.println("用户： " + senderName + " 发送消息： " + accMsg.getMsg() + " 给： " + accMsg.getSendToName() + "\t" + accMsg.getDate());
                            //保存到内存
//                            save(accMsg,senderName);
                            MessageRedis messageRedis = new MessageRedis();
                            messageRedis.setMsg(accMsg.getMsg());
                            messageRedis.setSendToName(accMsg.getSendToName());
                            messageRedis.setLength(accMsg.getLength());
                            messageRedis.setWriteTime(accMsg.getDate());

                            writeToRedis.setKey(senderName);
                            writeToRedis.setMessageRedis(messageRedis);
//                            writeToRedis.setMessageRedisList(msgList);
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



//    public  void save(Message accMsg,String senderName){
//        MessageRedis messageRedis = new MessageRedis();
//        messageRedis.setMsg(accMsg.getMsg());
//        messageRedis.setSendToName(accMsg.getSendToName());
//        messageRedis.setLength(accMsg.getLength());
//        messageRedis.setWriteTime(accMsg.getDate());
//        if(!msgMemoryMap.containsKey(senderName)){//第一次记录,则创建对应的消息列表 msgList 放入哈希表中
//            ArrayList<MessageRedis> msgList = new ArrayList<MessageRedis>();
//            msgList.add(messageRedis);
//            msgMemoryMap.put(senderName,msgList);
//        }else{//已经存在，则不是第一次记录
//            ArrayList<MessageRedis> arrayList = msgMemoryMap.get(senderName);
//            arrayList.add(messageRedis);
//        }
//        System.out.println("已经存入 msgMemoryMap 中");
//        ArrayList<MessageRedis> arrayList = msgMemoryMap.get(senderName);
//        System.out.println(arrayList.toString());
//    }


}
