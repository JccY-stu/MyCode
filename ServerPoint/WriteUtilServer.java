package com.yang.bioDPointObject.ServerPoint;


import com.yang.bioDPointObject.Message;

import java.io.*;
import java.net.Socket;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @Author Chengzhi
 * @Date 2021/11/3 10:59
 * @Version 1.0
 *
 * 服务器端使用
 *
 *
 * 作用：检测当前已连接的 Socket的数量
 */
public class WriteUtilServer implements Runnable{

    BufferedOutputStream bufferedOutputStream;
    //使用一个队列来保存已经连接的客户端的Socket（已修改为线程安全）
    ConcurrentLinkedQueue<Socket> connectedSocketList;
    //获取所有已回复的Socket列表,初始化标记数组
    ConcurrentHashMap<Socket,Integer> replyMap;
    //使用一个HashMap 存储 <姓名,Socket>
    ConcurrentHashMap<Socket,String> clientNameMap;

    public WriteUtilServer(ConcurrentLinkedQueue<Socket> connectedSocketList,ConcurrentHashMap<Socket,String> clientNameMap) throws IOException {
        this.connectedSocketList = connectedSocketList;
        this.replyMap = new ConcurrentHashMap<>();
        this.clientNameMap = clientNameMap;
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
            for (Iterator it = connectedSocketList.iterator(); it.hasNext(); ) {
                Socket curSocket = (Socket) it.next();
                if (!replyMap.containsKey(curSocket)) {//如果还没回复
                    //调用回复方法
                    try {
                        reply(bufferedOutputStream, clientNameMap, curSocket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //加入已回复Map
                    replyMap.put(curSocket, 1);
                }
            }
        }
    }


    /**
     * 反馈客户端，表示其连接已经成功！
     * 连接成功后根据情况进行反馈：
     *      1.已注册用户名,则返回""
     *      2.未注册用户名,则返回""
     *
     * @param bufferedOutputStream
     * @param socket
     */
    public static void reply(BufferedOutputStream bufferedOutputStream,ConcurrentHashMap<Socket,String> clientNameMap,Socket socket) throws IOException {
        try {
            bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        String msg;
        Message message = new Message();

        //回复之前判断该客户端是否已经注册
        if(clientNameMap.containsKey(socket)){
            //已注册
            message.setCode(200);
            message.setMsg("用户：" + clientNameMap.get(socket)+ "\t 您已成功连接服务器！");
        }else{
            //code = -1 表示需要注册
            message.setCode(-1);
            message.setMsg("您已成功连接服务器，但您还未注册用户名，请您输入您的用户名~");
        }
        byte[] bytes;
        //将对象序列化为字节数组
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(message);
        bytes = baos.toByteArray();
        System.out.println("序列化已成功！");

        try {
            bufferedOutputStream.write(bytes);
            bufferedOutputStream.flush();
        } catch (Exception e) {
            System.out.println("连接已中断!");
            e.printStackTrace();
        }
    }

    /**
     * 关闭流的正确写法
     *
     * @param closes
     */
    public static void close(Closeable... closes) {
        for (Closeable closeable : closes) {
            try {
                if(closeable != null){
                    closeable.close();
                    System.out.println("成功关闭socket");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
