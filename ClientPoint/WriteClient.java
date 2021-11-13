package com.yang.bioDPointObject.ClientPoint;

import com.yang.bioDPointObject.Entry.MsgCTC;
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

    //存放聊天室用户列表(全局变量)
    List clientList;

    //序列化实例
    SerializeUtil serializeUtil;

    //发送时间戳
    private long msgSendTime;

    //重传实例
    ResendClient resendClient;

    //心跳间隔
    private static  long HEART_BEAT_RATE = 30 * 1000;

    public WriteClient(Socket socket,List clientList,long msgSendTime,ResendClient resendClient) throws IOException {
        this.bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
        this.serializeUtil = new SerializeUtil();
        this.socket = socket;
        this.clientList = clientList;
        this.msgSendTime = msgSendTime;
        this.resendClient = resendClient;
    }

    public void run() {
        String msg;
        byte[] bytes;
        log.info("客户端可以开始写入要发送的信息了...");
        log.info("您的用户名为： ");
        log.info("如果您想更改私聊用户，请输入 alter");
        try {
            scanner = new Scanner(System.in);
            while (true) {
                System.out.println("请先选择需要私聊的用户：");//这里可以改成从List中取出，而非手动输入，避免用户发送给一个不存在的用户，即需要校验
                String accepterName = scanner.nextLine();
                if(!clientList.contains(accepterName)){//不存在该用户名
                    System.out.println("该用户名不存在啦！请重新选择");
                    continue;
                }
                //循环写入
                while (true) {
                    Thread.sleep(5);
                    System.out.println("请输入聊天内容：");
                    msg = scanner.nextLine();
                    if("alter".equals(msg))break;
                    Date date = new Date();
                    SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    //使用 msg.hashCode 可能会重复，加上日期的hashcode 作为每一条消息的唯一性序列号，重复的可能性很低
                    //如果需要完全避免，则使用uuid 这里采用上述办法
                    int uuid = String.valueOf(System.currentTimeMillis()).hashCode() + msg.hashCode();
                    //将对象序列化为字节数组
                    MsgCTC msgCTC = new MsgCTC(200,uuid,msg,accepterName,ft.format(date),(long) msg.getBytes().length);
                    bytes = serializeUtil.objectToByteArray(msgCTC);

                    //发送消息
                    try {
                        bufferedOutputStream.write(bytes);
                        bufferedOutputStream.flush();
                        System.out.println("To " + accepterName + ":" + msg  + "\t唯一序列号：" + uuid);
                        //发送之后，计时器开始计时
                        msgSendTime = System.currentTimeMillis();
                        resendClient.setMsgSendTime(msgSendTime);
                        resendClient.setMsgCTC(msgCTC);
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
