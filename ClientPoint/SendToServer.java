package com.yang.bioDPointObject.ClientPoint;

import com.yang.bioDPointObject.Message;
import com.yang.bioDPointObject.Util.serialize.SerializeUtil;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * @Author Chengzhi
 * @Date 2021/11/8 16:36
 * @Version 1.0
 */
public class SendToServer {

    //日志
    static Logger log = Logger.getLogger("SendToServer");

    //输出缓冲流
    BufferedOutputStream bufferedOutputStream;
    Socket socket;

    //指令内容
    Message message;

    //序列化
    SerializeUtil serializeUtil;

    public SendToServer(Socket socket,Message message) throws IOException {
        this.bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
        this.socket = socket;
        this.serializeUtil = new SerializeUtil();
        this.message = message;
    }

    /**
     * 获取当前聊天室人员列表
     */
    public void sendOrderToServer() throws IOException {
        log.info("请求获取当前客户端列表...");
        byte[] bytes;
        //将对象序列化为字节数组
        message = new Message(-2,null,0);//-2代表请求服务器端发送好友列表
        bytes = serializeUtil.objectToByteArray(message);

        //发送消息
        try {
            bufferedOutputStream.write(bytes);
            bufferedOutputStream.flush();
            log.info("请求已经发送完成啦");
        } catch (Exception e) {
            System.out.println("连接已中断!");
            e.printStackTrace();
        }
    }
}
