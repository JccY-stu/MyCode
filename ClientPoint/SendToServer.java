package com.yang.bioDPointObject.ClientPoint;

import com.yang.bioDPointObject.Entry.MsgCTS;
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
    MsgCTS msgCTS;

    //序列化
    SerializeUtil serializeUtil;

    public SendToServer(Socket socket) throws IOException {
        this.bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
        this.socket = socket;
        this.serializeUtil = new SerializeUtil();
        this.msgCTS = msgCTS;
    }

    /**
     * 获取当前聊天室人员列表
     */
    public void sendOrderToServer(MsgCTS msgCTS) throws IOException {
        log.info("请求获取当前客户端列表...");
        byte[] bytes;
        //将对象序列化为字节数组
        bytes = serializeUtil.objectToByteArray(msgCTS);

        //发送消息
        try {
            bufferedOutputStream.write(bytes);
            bufferedOutputStream.flush();
        } catch (Exception e) {
            log.info("连接已中断!");
            e.printStackTrace();
        }
    }
}
