package com.yang.bioDPointObject.Entry;

import java.io.Serializable;

/**
 * @Author Chengzhi
 * @Date 2021/11/11 18:23
 * @Version 1.0
 */
public class MsgACK extends Message implements Serializable {

    //消息类型
    private int code;
    //唯一序列号（表示每一条消息）
    private int uuid;
    //消息长度，用以分割消息
    private Long length;

    public MsgACK(int code, int uuid, Long length) {
        this.code = code;
        this.uuid = uuid;
        this.length = length;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getUuid() {
        return uuid;
    }

    public void setUuid(int uuid) {
        this.uuid = uuid;
    }

    public Long getLength() {
        return length;
    }

    public void setLength(Long length) {
        this.length = length;
    }
}
