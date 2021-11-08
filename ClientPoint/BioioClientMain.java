package com.yang.bioDPointObject.ClientPoint;

import com.yang.bioDPointObject.Message;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * @Author Chengzhi
 * @Date 2021/11/3 11:20
 * @Version 1.0
 *
 * 传递对象信息
 */
public class BioioClientMain {

    static Logger log = Logger.getLogger("BioioClientMain");

    private static final long HEART_BEAT_RATE = 30 * 1000;

    public static void main(String[] args) throws IOException, InterruptedException {

        //发送时间戳
        long sendTime = 0L;

        //创建与服务器绑定端口对应的套接字;
        //socket 通过 协议 + 端口 + ip 地址找到网络通信进程
        //如果是 Socket socket = new Socket("127.0.0.1",6666);则会自动进行连接
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress("127.0.0.1", 6666), 10000);//设置 10s 连接超时

        //创建一个新线程，用于 读取对端消息（其中完成了注册功能）
        ReadClient readUtil = new ReadClient(socket);
        Thread readThread = new Thread(readUtil);
        readThread.start();

        //创建请求服务器
        SendToServer sendToServer;
        Thread.sleep(10000);
        //告诉服务器 客户端需要获取当前聊天室所有人员名称
        sendToServer = new SendToServer(socket,new Message(-2,null,0));//后期可以将指令全部简化为 不需要序列化即可
        sendToServer.sendOrderToServer();

        //这里设置先注册 注册后20s才可以发送消息  实际上只需要第一次登录注册，然后如果要发送则将下列代码包装成方法调用即可，这里简化了逻辑1
        Thread.sleep(10000);
        //创建一个新线程 用于 发送数据
        WriteClient writeUtil = new WriteClient(socket, 0, sendTime);
        Thread writeThread = new Thread(writeUtil);
        writeThread.start();

        //发送心跳包
        //sendHeartBeat(socket);
    }

    /**
     * 发送心跳包
     */
    public static void sendHeartBeat(Socket socket) throws IOException {
        heartBeat heartBeat = new heartBeat(socket);
        new Thread(heartBeat).start();
    }

}
