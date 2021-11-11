package com.yang.bioDPointObject.ClientPoint;

import com.yang.bioDPointObject.Entry.Message;
import com.yang.bioDPointObject.Util.serialize.SerializeUtil;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * @Author Chengzhi
 * @Date 2021/11/3 10:59
 * @Version 1.0
 *
 * 发送消息
 */
public class WriteClient implements Runnable{

    Logger log = Logger.getLogger("WriteUtilClient");

    //输出缓冲流
    BufferedOutputStream bufferedOutputStream;
    Socket socket;
    Scanner scanner;
    int role;

    //存放聊天室用户列表(全局变量)
    List clientList;

    //序列化实例
    SerializeUtil serializeUtil;

    //发送时间戳
    private volatile long sendTime;

    //心跳间隔
    private static  long HEART_BEAT_RATE = 30 * 1000;

    public WriteClient(Socket socket, int role, long sendTime,List clientList) throws IOException {
        this.bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
        this.serializeUtil = new SerializeUtil();
        this.socket = socket;
        this.role = role;
        this.sendTime = sendTime;
        this.clientList = clientList;
    }

    public void run() {
        String msg;
        byte[] bytes;
        log.info("客户端可以开始写入要发送的信息了...");
        log.info("如果您想更改私聊用户，请输入 alter");
        try {
            scanner = new Scanner(System.in);
            while (true) {
                System.out.println("请先选择需要私聊的用户：");//这里可以改成从List中取出，而非手动输入，避免用户发送给一个不存在的用户，即需要校验
                String name = scanner.nextLine();
                if(!clientList.contains(name)){//不存在该用户名
                    System.out.println("该用户名不存在啦！请重新选择");
                    continue;
                }
                //循环写入
                while (true) {
                    System.out.println("请输入聊天内容：");
                    msg = scanner.nextLine();
                    if("alter".equals(msg))break;
                    Date date = new Date();
                    SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    //将对象序列化为字节数组
                    Message message = new Message(200,msg, name, (long) msg.getBytes().length,ft.format(date));
                    bytes = serializeUtil.objectToByteArray(message);

                    //发送消息
                    try {
                        bufferedOutputStream.write(bytes);
                        bufferedOutputStream.flush();
                        System.out.println("To " + name + ":" + msg);
                    } catch (Exception e) {
                        log.info("连接已中断!");
                        e.printStackTrace();
                    }
                }
            }
        }catch (Exception e) {
            log.info("写入消息时发现" + socket.getPort() + "连接中断了");
            e.printStackTrace();
        } finally {
            log.info("该关闭输出流了~");
        }
    }
}
