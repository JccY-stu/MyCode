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
    //接收对象
    private String sendToName;
    //消息长度，用以分割消息
    private Long length;
    //携带的对象类型
    private List<?> list;
    //写入日期
    private String date;

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    /**
     * 没有携带对象时 （客户端和服务器端互发）
     * @param code
     * @param msg
     * @param length
     */
    public Message(int code,String msg, long length){
        this.code = code;
        this.msg = msg;
        this.length = length;
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

    public String toString(){
        return "length:" + length + " " + "code:" + code + "\t" + "msg:" + msg + "\t" ;
    }

}
