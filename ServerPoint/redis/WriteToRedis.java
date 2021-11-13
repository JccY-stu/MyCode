package com.yang.bioDPointObject.ServerPoint.redis;


import com.yang.bioDPointObject.Entry.MessageRedis;
import com.yang.bioDPointObject.Entry.MsgRedisList;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

/**
 * @Author Chengzhi
 * @Date 2021/11/9 18:36
 * @Version 1.0
 *
 * 传入redis 操作实例 和发送消息的用户名
 * 将客户端发送的消息 写入 redis 进行保存
 */
public class WriteToRedis implements Runnable{

    static Logger log = Logger.getLogger("writeToRedis");

    //redis 使用实例
    Jedis jedis;

    //<K,v>键值对
    volatile String key;

    //写入对象
    volatile MessageRedis messageRedis;

    //写入消息列表
    volatile CopyOnWriteArrayList<MessageRedis> messageRedisList;

    public void setKey(String key) {
        this.key = key;
    }

    public void setMessageRedis(MessageRedis messageRedis) {
        this.messageRedis = messageRedis;
    }

    public void setMessageList(CopyOnWriteArrayList<MessageRedis> messageRedisList){
        this.messageRedisList = messageRedisList;
    }

    public WriteToRedis() throws IOException {
        this.jedis = new Jedis("localhost");;
    }

//    @Override
//    public void run() {
//        try {
//            Thread.sleep(30000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        log.info("写入 redis 的子线程已经开启...");
//        //连接本地的 Redis 服务
//        RedisOps redisOps = new RedisOps(jedis);
//        while (true) {
//            if (key != null && messageRedis != null) {//写入Redis
//                try {
//                    redisOps.setObject(key, this.messageRedis);
//                    ArrayList msgList = redisOps.getObject(key);
//                    log.info("写入Redis 成功！");
//                    key = null;
//                    messageRedis = null;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

    //上面的run是写入单个消息 ，会覆盖 例如 <橙子，“你好啊”> 而第二条放入的时候则会变成 <橙汁，“嘿嘿嘿”>
    //因此使用 List 变成 <橙汁，<你好啊，嘿嘿嘿>> 第二条消息不会覆盖第一条
    @Override
    public void run() {
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("写入 redis 的子线程已经开启...");
        //连接本地的 Redis 服务
        RedisOps redisOps = new RedisOps(jedis);
        MsgRedisList msgRedisList = new MsgRedisList();
        while (true) {
            if (key != null && messageRedisList != null) {//写入Redis
                try {
                    log.info(messageRedisList.toString());
                    msgRedisList.setCopyOnWriteArrayList((CopyOnWriteArrayList<MessageRedis>) messageRedisList);
                    redisOps.setObject(key,msgRedisList);
                    log.info("写入Redis 成功！");
                    key = null;
                    messageRedisList = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
