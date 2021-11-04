package com.yang.bioDPointObject;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author Chengzhi
 * @Date 2021/11/3 11:20
 * @Version 1.0
 *
 * 传递对象信息
 */
public class BioioServerObject {
    public static void main(String[] args) throws Exception{

        ServerSocket serverSocket = new ServerSocket(6666);
        System.out.println("服务器正在运行，等待客户端连接！");

        //使用线程池来创建线程
        ExecutorService threadPool = Executors.newFixedThreadPool(100);

        while(true){
            //监听，等待客户端连接
            System.out.println("等待连接....");
            final Socket socket = serverSocket.accept();
            System.out.println("连接到一个客户端");

            //创建一个新线程 用于 发送数据
            WriteUtilObject writeUtil = new WriteUtilObject(socket,1);
            threadPool.submit(writeUtil);

            //创建一个新线程 用于 读取数据
            ReadUtilObject readUtil = new ReadUtilObject(socket);
            threadPool.submit(readUtil);
        }
    }
}
