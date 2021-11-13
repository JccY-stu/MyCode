package com.yang.bioDPointObject.Entry;

import java.io.Serializable;


/**
 * @Author Chengzhi
 * @Date 2021/11/9 18:01
 * @Version 1.0
 *
 * 定义写入 Redis 的消息对象（存储）
 *
 * <发送人,消息信息>
 */
public class MessageRedis extends Message implements Serializable {

    //消息内容
    private String msg;
    //发送者名称
    private String senderName;
    //接收者名称
    private String sendToName;
    //消息长度，用以分割消息
    private Long length;
    //写入时间
    private String writeTime;


    public MessageRedis() {}

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSendToName() {
        return sendToName;
    }

    public void setSendToName(String sendToName) {
        this.sendToName = sendToName;
    }

    public Long getLength() {
        return length;
    }

    public void setLength(Long length) {
        this.length = length;
    }

    public String getWriteTime() {
        return writeTime;
    }

    public void setWriteTime(String writeTime) {
        this.writeTime = writeTime;
    }

    @Override
    public String toString() {
        return "MessageRedis{" +
                "msg='" + msg + '\'' +
                ", senderName='" + senderName + '\'' +
                ", sendToName='" + sendToName + '\'' +
                ", length=" + length +
                ", writeTime='" + writeTime + '\'' +
                '}';
    }
}
