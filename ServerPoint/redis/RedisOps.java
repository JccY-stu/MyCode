package com.yang.bioDPointObject.ServerPoint.redis;

import com.yang.bioDPointObject.Entry.MessageRedis;
import com.yang.bioDPointObject.Util.serialize.ProtostuffUtils;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.ArrayList;
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
     * 向 redis 中放入 <Key,Message>
     * @param key
     * @param messageRedis
     * @throws IOException
     */
    public void setObject(String key, MessageRedis messageRedis){
        jedis.set(key.getBytes(), ProtostuffUtils.serialize(messageRedis));
        log.info("将对象序列化并放入 Redis 中");
        jedis.close();
    }

    public void setObject(String key, ArrayList<MessageRedis> messageRedisList){
        jedis.set(key.getBytes(), ProtostuffUtils.serialize(messageRedisList));
        log.info("将对象序列化并放入 Redis 中");
        jedis.close();
    }

    public ArrayList getObject(String key){
        byte[] bytes = jedis.get(key.getBytes());
        log.info("将对象从 Redis 中 反序列化");
        jedis.close();
        return ProtostuffUtils.deserialize(bytes,ArrayList.class);
    }

    /**
     * 从 redis 中取出 <Key,Message>
     * @param key
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
//    public MessageRedis getObject(String key){
//        byte[] bytes = jedis.get(key.getBytes());
//        log.info("将对象从 Redis 中 反序列化");
//        jedis.close();
//        return ProtostuffUtils.deserialize(bytes,MessageRedis.class);
//    }

}
