package com.yang.bioDPointObject.ServerPoint;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author Chengzhi
 * @Date 2021/11/3 11:20
 * @Version 1.0
 *
 * 传递对象信息
 */
public class BioioServerMain {

    public static void main(String[] args) throws Exception{
        ServerSocket serverSocket = new ServerSocket(6666);
        System.out.println("服务器正在运行，等待客户端连接！");

        //使用一个队列来保存已经连接的客户端的Socket (已修改为线程安全)
        ConcurrentLinkedQueue<Socket> connectedSocketList = new ConcurrentLinkedQueue<Socket>();

        //使用一个HashMap 存储 <Socket,姓名>
        ConcurrentHashMap<Socket,String> clientNameMap = new ConcurrentHashMap<Socket, String>();

        //使用线程池来创建线程
        ExecutorService threadPool = Executors.newFixedThreadPool(100);

        //创建一个新线程，传入所有已连接的客户端的SocketList，循环反馈;
        WriteUtilServer writeUtilServer = new WriteUtilServer(connectedSocketList,clientNameMap);
        threadPool.submit(writeUtilServer);

        while(true){
            //监听，等待客户端连接
            System.out.println("等待连接....");
            final Socket socket = serverSocket.accept();
            System.out.println("连接到一个客户端");

            //将该客户端加入connetedSocketList
            connectedSocketList.add(socket);

            //创建一个新线程 用于 读取数据
            ReadUtilServer readUtil = new ReadUtilServer(socket,connectedSocketList);
            threadPool.submit(readUtil);
        }
    }
}
