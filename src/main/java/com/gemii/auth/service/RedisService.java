package com.gemii.auth.service;

import java.io.Serializable;

/**
 * @ClassName RedisService
 * @Description TODO
 * @Author wen.liu
 * @Date 2019-06-11 14:58
 **/
public interface RedisService extends Serializable {

    Object getStr(final Serializable key);

    boolean setStr(final Serializable key, Object value);

    boolean setStr(final Serializable key, Object value, Long expireTime);

    Long getExpire(final Serializable key);

    boolean setHashSet(final Serializable key, Object mapKey, Object mapValue);

    Object getHashSetValue(final Serializable key, final Object mapKey);
}
