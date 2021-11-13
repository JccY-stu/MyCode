package com.yang.bioDPointObject.ClientPoint;


import com.yang.bioDPointObject.Entry.Message;
import com.yang.bioDPointObject.Entry.MsgCTS;
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
    //重传线程实例
    ResendClient resendClient;
    //发送该条消息的时间
    long msgSendTime;
    //接收到ack的时间
    long ackReceiveTime;

    public ReadClient(Socket socket,List clientList,long msgSendTime,long ackReceiveTime,ResendClient resendClient) throws IOException {
        this.bufferedInputStream = new BufferedInputStream(socket.getInputStream());
        this.bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
        this.serializeUtil = new SerializeUtil();
        this.clientList = clientList;
        this.socket = socket;
        this.msgSendTime = msgSendTime;
        this.ackReceiveTime = ackReceiveTime;
        this.resendClient = resendClient;
    }

    //读取消息
    public void run() {
        byte[] bytes = new byte[1024];
        //通过socket，获取输入流;
        try {
                //循环的读取对端发送的数据
                while (true) {
                    resendClient.setAckReceiveTime(System.currentTimeMillis());
                    bufferedInputStream.read(bytes);

                    //将字节数组序列化为 Message 对象
                    Message msg = (Message) serializeUtil.byteArrayToObject(bytes);

                    //判断是服务器的回复还是其他客户端的回复
                    //服务器端的回复 code
                   if(msg.getCode() == -1 || msg.getCode() == 802){// -1 表示注册 802表示用户名已经存在 注册失败需要重新注册
                        System.out.println("From Server: "  + msg.toString());
                        //调用注册方法
                        new RegisterClient(socket).sendRegisterMsg();
                        continue;
                   }
                   if(msg.getCode() == -2){// -2 表示获取聊天室用户列表
                       //使用迭代器最安全 写入 clientList 中
                       Iterator it = msg.getList().iterator();
                       while(it.hasNext()) {
                           clientList.add(it.next());
                       }
                       System.out.println("From Server: 当前聊天室用户列表: "  + msg.getList().toString());
                       continue;
                   }
                   if(msg.getCode() == 801 || msg.getCode() == 201){// 801 表示接收者对象用户名不存在 201 表示用户注册成功
                       System.out.println("From Server: "  + msg.toString());
                       continue;
                   }
                   if(msg.getCode() == 100){//来自服务器回应的消息状态
                       resendClient.setFlag(1);
                       System.out.println("From Server:" + "唯一序列号为：" + msg.getUuid() +  "\t的消息发送成功！");
                       continue;
                   }else{
                       System.out.println("From OtherClient:" + msg.toString());
                   }
//                   if(System.currentTimeMillis() - msgSendTime > 500){//判定消息丢失
//                       //进行超时重发
//                       sendToServer.resend(new MsgCTS(200,"我是崇传消息",(long)0));
//                   }
//                   else{
////                        System.out.println("From OtherClient: " + socket.getInetAddress() + "\t消息为：" + msg.toString());
////                    }
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