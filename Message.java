package com.yang.bioDPointObject;


import java.io.Serializable;

/**
 * @Author Chengzhi
 * @Date 2021/11/3 15:29
 * @Version 1.0
 *
 * 定义消息对象
 */
public class Message implements Serializable {
    public int code;
    public String msg;

    public Message(int code,String msg){
        this.code = code;
        this.msg = msg;
    }


    public String toString(){
        return "对象类型为:" + Message.class.getName() + "    " + "code:" + code + "    "+ "msg:" + msg + "    ";
    }

}
