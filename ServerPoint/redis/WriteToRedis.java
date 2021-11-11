package com.yang.bioDPointObject.ServerPoint.redis;


import com.yang.bioDPointObject.Entry.MessageRedis;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.ArrayList;
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

    public void setKey(String key) {
        this.key = key;
    }

    public void setMessageRedis(MessageRedis messageRedis) {
        this.messageRedis = messageRedis;
    }

    public WriteToRedis() throws IOException {
        this.jedis = new Jedis("localhost");;
    }

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
        while (true) {
            if (key != null && messageRedis != null) {//写入Redis
                try {
                    redisOps.setObject(key, this.messageRedis);
                    ArrayList msgList = redisOps.getObject(key);
                    log.info("写入Redis 成功！");
                    key = null;
                    messageRedis = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
