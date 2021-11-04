package com.yang.bioDPointObject;

import java.io.IOException;
import java.net.Socket;

/**
 * @Author Chengzhi
 * @Date 2021/11/3 11:20
 * @Version 1.0
 *
 * 传递对象信息
 */
public class BioioClientObject {

    public static void main(String[] args) throws IOException {
        //创建与服务器绑定端口对应的套接字;
        //socket 通过 协议 + 端口 + ip 地址找到网络通信进程
        Socket socket = new Socket("127.0.0.1",6666);

        //创建一个新线程，用于 读取对端消息
        ReadUtilObject readUtil = new ReadUtilObject(socket);
        Thread readThread = new Thread(readUtil);
        readThread.start();

        //创建一个新线程 用于 发送数据
        WriteUtilObject writeUtil = new WriteUtilObject(socket,0);
        Thread writeThread = new Thread(writeUtil);
        writeThread.start();
    }
}
