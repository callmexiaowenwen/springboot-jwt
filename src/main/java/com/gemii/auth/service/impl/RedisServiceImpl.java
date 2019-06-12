package com.gemii.auth.service.impl;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import com.gemii.auth.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

/**
 * @ClassName RedisServiceImpl
 * @Description TODO
 * @Author wen.liu
 * @Date 2019-06-11 16:38
 **/
@Service
@Slf4j
public class RedisServiceImpl implements RedisService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Object getStr(Serializable key) {
        Object result;
        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
        result = operations.get(key);
        return result;
    }

    @Override
    public boolean setStr(Serializable key, Object value) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean setStr(Serializable key, Object value, Long expireTime) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Long getExpire(Serializable key) {
        return redisTemplate.getExpire(key);
    }

    @Override
    public boolean setHashSet(Serializable key, Object mapKey, Object mapValue) {
        return false;
    }

    @Override
    public Object getHashSetValue(Serializable key, Object mapKey) {
        return null;
    }
}
