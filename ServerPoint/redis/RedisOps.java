package com.yang.bioDPointObject.ServerPoint.redis;

import com.yang.bioDPointObject.Entry.MessageRedis;
import com.yang.bioDPointObject.Entry.MsgRedisList;
import com.yang.bioDPointObject.Util.serialize.ProtostuffUtils;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

/**
 * @Author Chengzhi
 * @Date 2021/11/9 17:43
 * @Version 1.0
 *
 * Redis 操作类
 */
public class RedisOps {

    static Logger log = Logger.getLogger("RedisOps");

    static Jedis jedis;

    public RedisOps(Jedis jedis){
        this.jedis = jedis;
    }

    /**
     * 向 redis 中存放 <Key,MsgRedisList>
     * @param key
     * @param msgRedisList
     */
    public void setObject(String key, MsgRedisList msgRedisList){
        byte[] bytes = ProtostuffUtils.serialize(msgRedisList);
        jedis.set(key.getBytes(),bytes);
        jedis.close();
    }

    /**
     * 从 redis 中 取出 <Key,MsgRedisList>
     * @param key
     * @return
     */
    public MsgRedisList getObject(String key) {
        byte[] bytes = jedis.get(key.getBytes());
        log.info("将对象从 Redis 中 反序列化");
        jedis.close();
        return ProtostuffUtils.deserialize(bytes,MsgRedisList.class);
    }

}
