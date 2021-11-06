package com.yang.bioDPointObject.ClientPoint;


import com.yang.bioDPointObject.Message;

import java.io.*;
import java.net.Socket;


/**
 * @Author Chengzhi
 * @Date 2021/11/3 11:11
 * @Version 1.0
 *
 * 读取消息
 */
public class ReadUtilClient implements Runnable {

    //输入缓冲流
    BufferedInputStream bufferedInputStream;
    //对端Socket
    Socket socket;

    public ReadUtilClient(Socket socket) throws IOException {
        this.bufferedInputStream = new BufferedInputStream(socket.getInputStream());
        this.socket = socket;
    }

    //读取消息
    public void run() {
        byte[] bytes = new byte[1024];
        //通过socket，获取输入流;
        try {
            while (true) {
                //循环的读取对端发送的数据
                while (true) {
                    bufferedInputStream.read(bytes);

                    //将字节数组序列化为 Message 对象
                    Message msg = (Message)byteArrayToObject(bytes);

                    //判断是服务器的回复还是其他客户端的回复
                    //服务器端的回复 code 都 < 0
                    if(msg.getCode() < 0){
                        System.out.println("From Server: "  + "\t消息为：" + msg.toString());
                    }else{
                        System.out.println("From OtherClient: " + socket.getInetAddress() + "\t消息为：" + msg.toString());
                    }
                }
            }
        }catch (Exception e) {
            System.out.println("读取消息时发现 服务器：" + socket.getPort() + "\t连接已中断");
            e.printStackTrace();
        }
        finally {
            close(socket);
        }
    }

    /**
     * 序列化
     * 将字节数组转换为 对象
     *
     * @param bytes 字节数组
     * @return Object 对象
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Object byteArrayToObject(byte[] bytes) throws IOException, ClassNotFoundException {
        //将字节数组序列化为 Message 对象
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        return objectInputStream.readObject();
    }

    /**
     * 关闭流的正确写法
     *
     * @param closes
     */
    public static void close(Closeable... closes) {
        for (Closeable closeable : closes) {
            try {
                if(closeable!=null){
                    closeable.close();
                    System.out.println("成功关闭输入流！");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
