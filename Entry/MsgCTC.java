package com.yang.bioDPointObject.Entry;

import java.io.Serializable;
import java.util.List;

/**
 * @Author Chengzhi
 * @Date 2021/11/11 17:40
 * @Version 1.0
 *
 * 定义客户端发送给另一个客户端的消息实体
 *
 * CTC : Client to  Client
 */
public class MsgCTC extends Message implements Serializable {

    //避免序列化不一致
    private static final long serialVersionUID = 1L;

    //消息类型
    private int code;
    //唯一序列号（表示每一条消息）
    private int uuid;
    //消息内容
    private String msg;
    //接收对象名称
    private String sendToName;
    //写入日期
    private String date;
    //消息长度，用以分割消息
    private Long length;

    public MsgCTC(int code, int uuid, String msg, String sendToName, String date, Long length) {
        this.uuid = uuid;
        this.code = code;
        this.msg = msg;
        this.sendToName = sendToName;
        this.date = date;
        this.length = length;
    }

    @Override
    public String toString() {
        return "MsgCTC{" +
                "code=" + code +
                ", uuid=" + uuid +
                ", msg='" + msg + '\'' +
                ", sendToName='" + sendToName + '\'' +
                ", date='" + date + '\'' +
                ", length=" + length +
                '}';
    }

    public int getUuid() {
        return uuid;
    }

    public void setUuid(int uuid) {
        this.uuid = uuid;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getLength() {
        return length;
    }

    public void setLength(Long length) {
        this.length = length;
    }
}
