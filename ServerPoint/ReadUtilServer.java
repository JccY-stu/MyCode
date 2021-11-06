package com.yang.bioDPointObject.ServerPoint;

/**
 * @Author Chengzhi
 * @Date 2021/11/6 0:30
 * @Version 1.0
 */

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @Author Chengzhi
 * @Date 2021/11/3 11:11
 * @Version 1.0
 *
 * 服务器端使用
 *
 * 读取消息
 */
public class ReadUtilServer implements Runnable {
    //输入缓冲流
    BufferedInputStream bufferedInputStream;
    //对端Socket
    Socket socket;
    //已经连接的客户端的SocketList (全局使用)
    ConcurrentLinkedQueue<Socket> connectedSocketList;

    public ReadUtilServer(Socket socket,ConcurrentLinkedQueue<Socket> connectedSocketList) throws IOException {
        this.bufferedInputStream = new BufferedInputStream(socket.getInputStream());
        this.socket = socket;
        this.connectedSocketList = connectedSocketList;
    }

    //读取消息
    public void run() {
        byte[] bytes = new byte[1024];
        //通过socket，获取输入流;
        try{
            while (true) {
                //循环的读取对端发送的数据
                while (true) {
//                    System.out.println("\nread....");
                    //首先读取两个字节表示的长度
                    int first = bufferedInputStream.read();
                    //如果读取的值为 -1 说明到了流的末尾，Socket已经被关闭了，此时将不能再去读取
                    if(first == -1){
                        break;
                    }
                    int second = bufferedInputStream.read();
                    int length = ((first << 8) + second);
                    bytes = new byte[length];
                    bufferedInputStream.read(bytes);

//                    //将字节数组序列化为对象
//                    ObjectInputStream oInputStream = new ObjectInputStream(inputStream);
//                    Message accMsg = (Message) oInputStream.readObject();


                    System.out.println("get message from Client Point: "+ socket.getInetAddress() + "消息为：" + new String(bytes, "UTF-8"));

//                    //只有当客户端关闭它的输出流的时候，服务器才能取得结尾的-1
//                    if (read != -1) {
//                        System.out.print("对方 says:" + new String(bytes, 0, read));//输出对端发送的数据
//                    }
                    }
                }
            }catch (Exception e) {
                connectedSocketList.remove(socket);
                System.out.println("读取消息时发现 客户端端口号：" + socket.getPort() + "\t连接已中断");
                e.printStackTrace();
            }
            finally {
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
