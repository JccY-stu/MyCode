package com.yang.bioDPointObject;


import jdk.nashorn.internal.objects.annotations.Getter;

import java.io.Serializable;

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

    //判断是什么类型的消息
    private int code;
    //消息内容
    private String msg;

    public Message(){}


    public Message(int code,String msg){
        this.code = code;
        this.msg = msg;
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
        return "code:" + code + "  "+ "msg:" + msg;
    }

}
