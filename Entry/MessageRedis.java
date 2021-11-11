package com.yang.bioDPointObject.Entry;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author Chengzhi
 * @Date 2021/11/9 18:01
 * @Version 1.0
 *
 * 定义写入 Redis 的消息对象（存储）
 *
 * <发送人,消息信息>
 */
public class MessageRedis implements Serializable {

    //消息内容
    private String msg;
    //接收者名称
    private String sendToName;
    //消息长度，用以分割消息
    private Long length;
    //写入时间
    private String writeTime;


    public MessageRedis() {}

    /**
     * 存入 redis 的对象
     * @param msg 信息
     * @param sendToName 接收者
     * @param length 信息长度
     * @param writeTime 写入时间
     */
    public MessageRedis(String msg, String sendToName, Long length, String writeTime) {
        this.msg = msg;
        this.sendToName = sendToName;
        this.length = length;
        this.writeTime = writeTime;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
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
                ", sendToName='" + sendToName + '\'' +
                ", length=" + length +
                ", writeTime='" + writeTime + '\'' +
                '}';
    }
}
