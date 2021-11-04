package com.yang.bioDPointObject;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * @Author Chengzhi
 * @Date 2021/11/3 10:59
 * @Version 1.0
 *
 * 发送消息
 */
public class WriteUtilObject implements Runnable{

    BufferedOutputStream bufferedOutputStream;
    Socket socket;
    Scanner scanner;
    int role;

    public WriteUtilObject(Socket socket,int role) throws IOException {
        this.socket = socket;
        this.bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
        this.role = role;
    }

    public void run(){
        String msg;
        if(role == 1){
            msg = "已成功连接客户端！";
        }else{
            msg = "成功连接到服务器！";
        }
        byte[] bytes = msg.getBytes();
        try {
            bufferedOutputStream.write(bytes);
            bufferedOutputStream.flush();
            //循环写入
            while (true) {
                scanner = new Scanner(System.in);
                msg = scanner.nextLine();

                //将对象序列化为字节数组
//                Message message = new Message(200,msg);
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                ObjectOutputStream oos = new ObjectOutputStream(baos);
//                oos.writeObject(message);
//                bytes = baos.toByteArray();

                //首先需要计算得知消息的长度
                bytes = msg.getBytes("UTF-8");
                //然后将消息发送出去
                //首先发送消息的长度
                bufferedOutputStream.write(bytes.length >> 8);
                bufferedOutputStream.write(bytes.length);
                //然后发送消息
                bufferedOutputStream.write(bytes);
                bufferedOutputStream.flush();
                //    System.out.println("请输入消息：");
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            System.out.println("该关闭输出流了~");
            close(socket);
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
