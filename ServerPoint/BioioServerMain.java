package com.yang.bioDPointObject.ServerPoint;

import com.yang.bioDPointObject.Entry.MessageRedis;
import com.yang.bioDPointObject.ServerPoint.redis.WriteToRedis;


import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**
 * @Author Chengzhi
 * @Date 2021/11/3 11:20
 * @Version 1.0
 *
 * 传递对象信息
 */
public class BioioServerMain {

    static Logger log = Logger.getLogger("BioioServerMain");

    public static void main(String[] args) throws Exception{
        ServerSocket serverSocket = new ServerSocket(6666);
        log.info("服务器正在运行，等待客户端连接！");

        //使用一个队列来保存已经连接的客户端的Socket (已修改为线程安全)
        ConcurrentLinkedQueue<Socket> connectedSocketList = new ConcurrentLinkedQueue<Socket>();

        //使用一个HashMap 存储 <Socket,姓名>
        ConcurrentHashMap<Socket,String> clientNameMap = new ConcurrentHashMap<Socket, String>();

        //使用一个HashMap 存储消息 <发送者名称,发送的消息列表> <senderName,MessageRedis>
        ConcurrentHashMap<String, Integer> msgMemoryMap = new ConcurrentHashMap<String, Integer>();

        //使用一个List 存储消息 <发送的消息列表> <senderName,MessageRedis>
        ArrayList<MessageRedis> everySenderMsgList = new ArrayList<MessageRedis>();

        //使用线程池来创建线程
        ExecutorService threadPool = Executors.newFixedThreadPool(100);

        //创建一个新线程，传入所有已连接的客户端的SocketList，循环反馈;
        WriteServer writeServer = new WriteServer(connectedSocketList,clientNameMap);
        threadPool.submit(writeServer);

        //创建一个写入 redis 的线程
        WriteToRedis writeToRedis = new WriteToRedis();
        threadPool.submit(writeToRedis);

        while(true){
            //监听，等待客户端连接
            log.info("等待连接....");
            final Socket socket = serverSocket.accept();
            log.info("连接到一个客户端");

            //将该客户端加入connetedSocketList(已连接链表)
            connectedSocketList.add(socket);

            //创建一个新线程 用于 读取数据
            ReadServer readUtil = new ReadServer(socket,connectedSocketList,clientNameMap,msgMemoryMap,everySenderMsgList,writeToRedis);
            threadPool.submit(readUtil);
        }
    }
}
