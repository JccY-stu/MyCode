package com.yang.bioDPointObject.ClientPoint;


import com.yang.bioDPointObject.Entry.Message;
import com.yang.bioDPointObject.Util.close.CloseUtil;
import com.yang.bioDPointObject.Util.serialize.SerializeUtil;


import java.io.*;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;


/**
 * @Author Chengzhi
 * @Date 2021/11/3 11:11
 * @Version 1.0
 *
 * 读取消息
 */
public class ReadClient implements Runnable {

    static Logger log = Logger.getLogger("ReadUtilClient");

    //输入缓冲流
    BufferedInputStream bufferedInputStream;
    //输出缓冲流
    BufferedOutputStream bufferedOutputStream;
    //对端Socket
    Socket socket;
    //当前聊天室用户列表
    List clientList;
    //序列化实例
    SerializeUtil serializeUtil;


    public ReadClient(Socket socket,List clientList) throws IOException {
        this.bufferedInputStream = new BufferedInputStream(socket.getInputStream());
        this.bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
        this.serializeUtil = new SerializeUtil();
        this.clientList = clientList;
        this.socket = socket;
    }

    //读取消息
    public void run() {
        byte[] bytes = new byte[1024];
        //通过socket，获取输入流;
        try {
                //循环的读取对端发送的数据
                while (true) {
                    bufferedInputStream.read(bytes);

                    //将字节数组序列化为 Message 对象
                    Message msg = (Message) serializeUtil.byteArrayToObject(bytes);

                    //判断是服务器的回复还是其他客户端的回复
                    //服务器端的回复 code
                   if(msg.getCode() == -1){// -1 表示注册
                        System.out.println("From Server: "  + msg.toString());
                        //调用注册方法
                        new RegisterClient(socket).sendRegisterMsg();
                   }
                   if(msg.getCode() == -2){// -2 表示获取聊天室用户列表
                       //使用迭代器最安全 写入 clientList 中
                       Iterator it = msg.getList().iterator();
                       while(it.hasNext()) {
                           clientList.add(it.next());
                       }
                       System.out.println("From Server: 当前聊天室用户列表: "  + msg.getList().toString());
                   }
                   if(msg.getCode() == 801){// 801 表示该用户名不存在
                       System.out.println("From Server: "  + msg.toString());
                   }
//                   else{
//                        System.out.println("From OtherClient: " + socket.getInetAddress() + "\t消息为：" + msg.toString());
//                    }
                }
        }catch (Exception e) {
            log.info("读取消息时发现 服务器：" + socket.getPort() + "\t连接已中断");
            e.printStackTrace();
        }
        finally {
            CloseUtil.close(socket);
        }
    }

}