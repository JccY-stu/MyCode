package com.yang.bioDPointObject.ClientPoint;

import com.yang.bioDPointObject.ClientPoint.ReadUtilClient;
import com.yang.bioDPointObject.ClientPoint.WriteUtilClient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @Author Chengzhi
 * @Date 2021/11/3 11:20
 * @Version 1.0
 *
 * 传递对象信息
 */
public class BioioClientMain {

    public static void main(String[] args) throws IOException {
        //创建与服务器绑定端口对应的套接字;
        //socket 通过 协议 + 端口 + ip 地址找到网络通信进程

        //如果是 Socket socket = new Socket("127.0.0.1",6666);则会自动进行连接
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress("127.0.0.1",6666),10000);//设置 10s 连接超时

        //创建一个新线程，用于 读取对端消息

        ReadUtilClient readUtil = new ReadUtilClient(socket);
        Thread readThread = new Thread(readUtil);
        readThread.start();

        //创建一个新线程 用于 发送数据
        WriteUtilClient writeUtil = new WriteUtilClient(socket,0);
        Thread writeThread = new Thread(writeUtil);
        writeThread.start();
    }
}
