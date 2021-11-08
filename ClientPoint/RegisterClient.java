package com.yang.bioDPointObject.ClientPoint;

import com.yang.bioDPointObject.Message;
import com.yang.bioDPointObject.Util.serialize.SerializeUtil;
import jdk.nashorn.internal.ir.CallNode;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * @Author Chengzhi
 * @Date 2021/11/8 14:02
 * @Version 1.0
 *
 * 客户端完成注册功能
 *
 * 发送注册信息
 */
public class RegisterClient {

    static Logger log = Logger.getLogger("RegisterClient");

    //输入缓冲流
    BufferedInputStream bufferedInputStream;
    //输出缓冲流
    BufferedOutputStream bufferedOutputStream;
    //对端Socket
    Socket socket;

    //序列化实例
    SerializeUtil serializeUtil;


    public RegisterClient(Socket socket) throws IOException {
        this.bufferedInputStream = new BufferedInputStream(socket.getInputStream());
        this.bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
        this.serializeUtil = new SerializeUtil();
        this.socket = socket;
    }

    /**
     * 实现注册
     */
    public void sendRegisterMsg() throws IOException {
        System.out.println("请输入您注册的用户名：");
        Scanner scanner = new Scanner(System.in);
        String msg = scanner.nextLine();
        //将对象序列化为字节数组
        Message message = new Message(-1, msg, msg.getBytes().length);
        byte[] bytes = serializeUtil.objectToByteArray(message);
        //发送消息
        try {
            bufferedOutputStream.write(bytes);
            bufferedOutputStream.flush();
            System.out.println("您的注册信息已经成功发送~");
        } catch (Exception e) {
            log.info("连接已中断!");
            e.printStackTrace();
        }
    }
}