package com.yang.bioDPointObject.Entry;

import java.io.Serializable;

/**
 * @Author Chengzhi
 * @Date 2021/11/11 17:41
 * @Version 1.0
 */
public class MsgCTS extends Message implements Serializable {

    //消息类型
    private int code;
    //消息内容
    private String msg;
    //消息长度，用以分割消息
    private Long length;

    public MsgCTS(int code, String msg, Long length) {
        this.code = code;
        this.msg = msg;
        this.length = length;
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

    public Long getLength() {
        return length;
    }

    public void setLength(Long length) {
        this.length = length;
    }

    @Override
    public String toString() {
        return "MsgCTS{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", length=" + length +
                '}';
    }

}
