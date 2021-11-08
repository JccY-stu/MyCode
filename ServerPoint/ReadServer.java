package com.yang.bioDPointObject.ServerPoint;

/**
 * @Author Chengzhi
 * @Date 2021/11/6 0:30
 * @Version 1.0
 */

import com.yang.bioDPointObject.Message;
import com.yang.bioDPointObject.Util.close.CloseUtil;
import com.yang.bioDPointObject.Util.serialize.SerializeUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;
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

    //序列化实例
    SerializeUtil serializeUtil;

    //返回响应
    ResponseToClient responseToClient;

    public ReadServer(Socket socket, ConcurrentLinkedQueue<Socket> connectedSocketList, ConcurrentHashMap<Socket,String> clientNameMap) throws IOException {
        this.bufferedInputStream = new BufferedInputStream(socket.getInputStream());
        this.bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
        this.serializeUtil = new SerializeUtil();
        this.socket = socket;
        this.connectedSocketList = connectedSocketList;
        this.clientNameMap = clientNameMap;
        this.responseToClient = new ResponseToClient(socket,clientNameMap);
    }

    //读取消息
    public void run() {
        byte[] bytes = new byte[1024];
        //通过socket，获取输入流;
        try{
            while (true) {
                //循环的读取对端发送的数据
                while (true) {
                    bufferedInputStream.read(bytes);

                    //将字节数组序列化为对象
                    Message accMsg = serializeUtil.byteArrayToObject(bytes);

                    //判断消息类型
                    //根据 code 来
                    if (accMsg.getCode() == -1) {
                        System.out.println("Client Point " + socket.getInetAddress() + "\t请求注册：" + "注册用户名为：" + accMsg.getMsg());
                        //调用注册方法
                        responseToClient.Register(accMsg.getMsg());
                    }
                    if(accMsg.getCode() == -2){// -2 表示返回当前聊天室客户端名称列表
                        System.out.println("Client Point " + socket.getInetAddress() + "\t请求获取：" + "当前聊天室所有用户名称");
                        //返回当前用户列表
                        responseToClient.responseClientList(socket,clientNameMap);
                    }
                    if (accMsg.getCode() == 200) {
                        System.out.println("Client Point: " + clientNameMap.get(socket) + "发送消息：" + accMsg.toString());
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




}
