package com.yang.bioDPointObject.ClientPoint;

import com.yang.bioDPointObject.Message;
import com.yang.bioDPointObject.Util.close.CloseUtil;
import com.yang.bioDPointObject.Util.serialize.SerializeUtil;

import java.io.*;
import java.net.Socket;
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

    //序列化实例
    SerializeUtil serializeUtil;

    //发送时间戳
    private volatile long sendTime;

    //心跳间隔
    private static  long HEART_BEAT_RATE = 30 * 1000;

    public WriteClient(Socket socket, int role, long sendTime) throws IOException {
        this.bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
        this.serializeUtil = new SerializeUtil();
        this.socket = socket;
        this.role = role;
        this.sendTime = sendTime;
    }

    public void run() {
        String msg;
        byte[] bytes;
        log.info("客户端可以开始写入要发送的信息了...");
        try {
            //循环写入
            while (true) {
                scanner = new Scanner(System.in);
                msg = scanner.nextLine();

                //将对象序列化为字节数组
                Message message = new Message(200, msg, msg.getBytes().length);
                bytes = serializeUtil.objectToByteArray(message);

                //发送消息
                try {
                    bufferedOutputStream.write(bytes);
                    bufferedOutputStream.flush();
                } catch (Exception e) {
                    System.out.println("连接已中断!");
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            System.out.println("写入消息时发现" + socket.getPort() + "连接中断了");
            e.printStackTrace();
        } finally {
            System.out.println("该关闭输出流了~");
        }
    }
}
