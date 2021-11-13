package com.yang.bioDPointObject.ClientPoint;

import com.yang.bioDPointObject.Entry.MsgCTC;
import com.yang.bioDPointObject.Util.serialize.SerializeUtil;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * @Author Chengzhi
 * @Date 2021/11/12 10:56
 * @Version 1.0
 */
public class ResendClient implements Runnable{

    Logger log = Logger.getLogger("ResendClient");

    //输出缓冲流
    BufferedOutputStream bufferedOutputStream;

    //序列化实例
    SerializeUtil serializeUtil;

    //重发消息
    volatile MsgCTC msgCTC;

    //服务器Socket
    Socket socket;

    //发送时间戳
    volatile long msgSendTime;

    //接收回应时间戳
    volatile long ackReceiveTime;

    volatile int flag;

    public void setMsgCTC(MsgCTC msgCTC) {
        this.msgCTC = msgCTC;
    }

    public void setMsgSendTime(long msgSendTime) {
        this.msgSendTime = msgSendTime;
    }

    public void setAckReceiveTime(long ackReceiveTime) {
        this.ackReceiveTime = ackReceiveTime;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public ResendClient(Socket socket) throws IOException {
        this.bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
        this.socket = socket;
        this.serializeUtil = new SerializeUtil();
    }

    @Override
    public void run() {
        log.info("重传消息线程已启动...");
        while (true) {
            if(msgSendTime != 0 && msgCTC != null && System.currentTimeMillis() - msgSendTime > 500 && flag == 0) {
                log.info("开始重传消息...");
                try{
                    byte[] bytes;
                    //将对象序列化为字节数组
                    bytes = serializeUtil.objectToByteArray(msgCTC);
                    bufferedOutputStream.write(bytes);
                    bufferedOutputStream.flush();
                    log.info("消息重传成功！" + msgCTC.toString());
                    msgCTC = null;
                    flag = 0;
                } catch (Exception e) {
                    log.info("连接已中断!");
                    e.printStackTrace();
                }
            }
        }
    }
}
