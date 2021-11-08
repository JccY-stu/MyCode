package com.yang.bioDPointObject;




import java.io.Serializable;
import java.util.List;

/**
 * @Author Chengzhi
 * @Date 2021/11/3 15:29
 * @Version 1.0
 *
 * 定义消息对象
 */
public class Message implements Serializable{
    //避免序列化不一致
    private static final long serialVersionUID = 1L;

    //消息类型
    private int code;
    //消息内容
    private String msg;
    //消息长度，用以分割消息
    private Long length;
    //携带的对象类型
    private List<?> list;

    public Message(){}


    public Long getLength() {
        return length;
    }

    public void setLength(Long length) {
        this.length = length;
    }

    /**
     * 没有携带对象时
     * @param code
     * @param msg
     * @param length
     */
    public Message(int code, String msg, long length){
        this.code = code;
        this.msg = msg;
        this.length = length;
    }

    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }

    /**
     * 携带对象时
     * @param code
     * @param msg
     * @param length
     */
    public Message(int code, String msg, long length,List object){
        this.code = code;
        this.msg = msg;
        this.length = length;
        this.list = object;
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
        return "length:" + length + "\t" + "code:" + code + "\t" + "msg:" + msg + "\t" ;
    }

    public String toStringAndT(){
        return "length:" + length + "\t" + "code:" + code + "\t" + "msg:" + msg + "\t" + "object:" + list;
    }
}
