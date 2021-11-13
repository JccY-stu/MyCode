package com.yang.bioDPointObject.Entry;






import java.io.Serializable;
import java.util.List;

/**
 * @Author Chengzhi
 * @Date 2021/11/3 15:29
 * @Version 1.0
 *
 * 定义消息对象
 */
public class Message implements Serializable {
    //避免序列化不一致
    private static final long serialVersionUID = 1L;

    //消息类型
    private int code;
    //消息内容
    private String msg;
    //发送者名称
    private String senderName;
    //接收对象
    private String sendToName;
    //消息长度，用以分割消息
    private Long length;
    //携带的对象类型
    private List<?> list;
    //写入日期
    private String date;
    //唯一序列号（表示每一条消息）
    private int uuid;

    public Message(){}

    /**
     * 发送给其他客户端
     * @param code
     * @param msg
     * @param sendToName
     * @param length
     * @param date
     */
    public Message(int code, String msg, String sendToName, Long length, String date) {
        this.code = code;
        this.msg = msg;
        this.sendToName = sendToName;
        this.length = length;
        this.date = date;
    }

    /**
     * 携带对象时（用于服务器向客户端返回 当前聊天室用户列表）
     * @param code
     * @param msg
     * @param length
     * @param object 所携带的对象
     */
    public Message(int code, String msg, long length,List object){
        this.code = code;
        this.msg = msg;
        this.length = length;
        this.list = object;
    }

    public int getUuid() {
        return uuid;
    }

    public void setUuid(int uuid) {
        this.uuid = uuid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public void setSendToName(String name) {
        this.sendToName = name;
    }

    public Long getLength() {
        return length;
    }

    public void setLength(Long length) {
        this.length = length;
    }

    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
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

    /**
     * 服务器端或者客户端发来的消息
     * @return
     */
    public String toString(){
        return "code:" + code + "\t" + "msg:" + msg;
    }

}
