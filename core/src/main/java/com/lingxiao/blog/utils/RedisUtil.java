package com.lingxiao.blog.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author lingxiao
 * @date ：Created in 2019/9/4 16:27
 * @description：redis工具类
 */
@Component
public class RedisUtil {

    public RedisTemplate redisTemplate = null;

    @Autowired(required = false)
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        RedisSerializer<String> stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        //redisTemplate.setValueSerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        redisTemplate.setHashValueSerializer(stringSerializer);
        this.redisTemplate = redisTemplate;
    }

    /**
     * 根据key删除
     *
     * @param key
     */
    public void delRedis(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 根据key获取list
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public <T> List<T> getListByKey(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    public <T> List<T> getListByKey(String key) {
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    /**
     * 向右添加
     *
     * @param key
     * @param list
     */
    public void rightPushAll(String key, List list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        // 先删除再push
        delRedis(key);
        redisTemplate.opsForList().rightPushAll(key, list);
    }

    public void rightPushAll(String key, List list,long time) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        // 先删除再push
        delRedis(key);
        redisTemplate.opsForList().rightPushAll(key, list);
        redisTemplate.expire(key,time, TimeUnit.MILLISECONDS);
    }

    //private Gson gson = new Gson();
    public <T> void pushValue(String key, T data) {
        /*Type type = new TypeToken<T>() {
        }.getType();
        String json = gson.toJson(data, type);*/
        redisTemplate.opsForValue().set(key, data);

    }

    public <T> void pushValue(String key, T data, long time) {
        redisTemplate.opsForValue().set(key, data,time,TimeUnit.MILLISECONDS);
    }

    public <T> T getValueByKey(String key) {
        Object obj = redisTemplate.opsForValue().get(key);
        if (obj == null) {
            return null;
        }
        return (T) obj;
    }

    public boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }

    public long incr(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }
}