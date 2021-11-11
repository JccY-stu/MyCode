package com.yang.bioDPointObject.ServerPoint.redis;

import com.yang.bioDPointObject.Entry.Message;
import redis.clients.jedis.Jedis;

import java.io.IOException;

/**
 * @Author Chengzhi
 * @Date 2021/11/9 15:49
 * @Version 1.0
 */


public class RedisTest {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        //连接本地的 Redis 服务
        Jedis jedis = new Jedis("localhost");
        RedisOps redisOps = new RedisOps(jedis);
        Message message = new Message(-1, "OKK", 0);
//        redisOps.setObject("橙汁",message);
//        Message msg = redisOps.getObject("橙汁");
//        System.out.println(msg.toString());
        // 如果 Redis 服务设置了密码，需要下面这行，没有就不需要
        // jedis.auth("123456");
//        System.out.println("连接成功");
//        //查看服务是否运行
//        System.out.println("服务正在运行: "+jedis.ping());
    }
}
