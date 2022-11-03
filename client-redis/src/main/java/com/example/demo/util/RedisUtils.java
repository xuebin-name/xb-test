package com.example.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtils {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 获取字符key
     * @param key
     * @return
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    public void set(String key, Object value,long time){
        redisTemplate.opsForValue().set(key,value,time, TimeUnit.SECONDS);
    }

    /**
     * 分布式锁
     * @param key
     * @param timeout
     * @return
     */
    public boolean tryLock(String key,long timeout){
        String uuid = UUID.randomUUID().toString();
        redisTemplate.execute((RedisCallback) connect->{
            //long expireTime = System.currentTimeMillis();
            Boolean aBoolean = connect.setNX(key.getBytes(), uuid.getBytes());
            if(aBoolean){
                connect.expire(key.getBytes(),timeout);
                System.out.println("加锁成功");
                return aBoolean;
            }
            System.out.println("加锁失败");
            return aBoolean;
        });
        return false;
    }



}
