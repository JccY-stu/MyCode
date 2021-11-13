package com.yang.bioDPointObject.Entry;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Author Chengzhi
 * @Date 2021/11/13 11:20
 * @Version 1.0
 */
public class MsgRedisList {


    CopyOnWriteArrayList<MessageRedis> copyOnWriteArrayList;


    public MsgRedisList() {}

    public MsgRedisList(CopyOnWriteArrayList<MessageRedis> copyOnWriteArrayList) {
        this.copyOnWriteArrayList = copyOnWriteArrayList;
    }

    public CopyOnWriteArrayList getCopyOnWriteArrayList() {
        return copyOnWriteArrayList;
    }

    public void setCopyOnWriteArrayList(CopyOnWriteArrayList<MessageRedis> copyOnWriteArrayList) {
        this.copyOnWriteArrayList = copyOnWriteArrayList;
    }


}
