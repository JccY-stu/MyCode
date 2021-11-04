package com.yang.bioDPointObject;

import java.io.*;
import java.net.Socket;

/**
 * @Author Chengzhi
 * @Date 2021/11/3 11:11
 * @Version 1.0
 *
 * 读取消息
 */
public class ReadUtilObject implements Runnable {
    InputStream inputStream;
    Socket socket;

    public ReadUtilObject(Socket socket) throws IOException {
        this.inputStream = socket.getInputStream();
        this.socket = socket;
    }

    //读取消息
    public void run() {
        byte[] bytes = new byte[1024];
        //通过socket，获取输入流;
        while (true) {
            try {
                //循环的读取对端发送的数据
                while (true) {
//                    System.out.println("\nread....");
                    //首先读取两个字节表示的长度
                    int first = inputStream.read();
                    //如果读取的值为 -1 说明到了流的末尾，Socket已经被关闭了，此时将不能再去读取
                    if(first == -1){
                        break;
                    }
                    int second = inputStream.read();
                    int length = ((first << 8) + second);
                    bytes = new byte[length];
                    inputStream.read(bytes);

//                    //将字节数组序列化为对象
//                    ObjectInputStream oInputStream = new ObjectInputStream(inputStream);
//                    Message accMsg = (Message) oInputStream.readObject();


                    System.out.println("get message from Point: " + new String(bytes, "UTF-8") + socket.getInetAddress() + socket.getPort());

//                    //只有当客户端关闭它的输出流的时候，服务器才能取得结尾的-1
//                    if (read != -1) {
//                        System.out.print("对方 says:" + new String(bytes, 0, read));//输出对端发送的数据
//                    }
                }
            } catch (Exception e) {
                System.out.println("连接已中断");
                return;
            }
            finally {
                close(socket);
            }
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
