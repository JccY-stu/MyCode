package com.yang.bioDPointObject.ServerPoint;


import com.yang.bioDPointObject.Util.serialize.SerializeUtil;

import java.io.*;
import java.net.Socket;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

/**
 * @Author Chengzhi
 * @Date 2021/11/3 10:59
 * @Version 1.0
 *
 * 服务器端使用
 *
 *
 * 作用：检测并更新当前已连接的 Socket的数量 对已连接的Socket进行回复
 */
public class WriteServer implements Runnable{

    static Logger log = Logger.getLogger("WriteUtilServer");

    BufferedOutputStream bufferedOutputStream;
    //使用一个队列来保存已经连接的客户端的Socket（已修改为线程安全）
    ConcurrentLinkedQueue<Socket> connectedSocketList;
    //获取所有已回复的Socket列表,初始化标记数组
    ConcurrentHashMap<Socket,Integer> replyMap;
    //使用一个HashMap 存储 <姓名,Socket>
    ConcurrentHashMap<Socket,String> clientNameMap;
    //序列化实例
    SerializeUtil serializeUtil;

    //回应客户端实例
    ResponseToClient responseToClient;

    public WriteServer(ConcurrentLinkedQueue<Socket> connectedSocketList, ConcurrentHashMap<Socket,String> clientNameMap) throws IOException {
        this.serializeUtil = new SerializeUtil();
        this.connectedSocketList = connectedSocketList;
        this.replyMap = new ConcurrentHashMap<>();
        this.clientNameMap = clientNameMap;
        this.responseToClient = new ResponseToClient(connectedSocketList,clientNameMap);
    }

    //向已连接的所有Socket发送响应消息,并加入到replyMap(在其中的表示已响应)
    public void run() {
        while (true) {
            try {
                Thread.sleep(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //当前没有客户端Socket连接服务器端，也就是无需回应
            if (connectedSocketList.size() == 0) continue;

            //当前已连接服务器的客户端数量大于 0
            for (Iterator it = connectedSocketList.iterator(); it.hasNext();) {
                Socket curSocket = (Socket) it.next();
                if (!replyMap.containsKey(curSocket)) {//如果还没回复
                    //调用回复方法
                    try {
                        responseToClient.responseConnect(bufferedOutputStream,curSocket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //加入已回复Map
                    replyMap.put(curSocket, 1);
                }
            }
        }
    }
}
