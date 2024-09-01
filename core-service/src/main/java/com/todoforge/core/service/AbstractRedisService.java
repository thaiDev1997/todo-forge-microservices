package com.todoforge.core.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
@RequiredArgsConstructor
public abstract class AbstractRedisService {

    RedisTemplate redisTemplate;

    public <T> void hashPut(String key, String hashKey, T domain) {
        redisTemplate.opsForHash().put(key, hashKey, domain);
    }

    public <T> T hashGet(String key, String hashKey) {
        return (T) redisTemplate.opsForHash().get(key, hashKey);
    }

    public void hashRemove(String key, String hashKey) {
        redisTemplate.opsForHash().delete(key, hashKey);
    }

    public <T> Long listPush(String key, T domain) {
        return redisTemplate.opsForList().rightPush(key, domain);
    }

    public <T> Long listPush(String key, Collection<T> domain) {
        return redisTemplate.opsForList().rightPushAll(key, domain);
    }

    public <T> Long listUnshift(String key, T domain) {
        return redisTemplate.opsForList().leftPush(key, domain);
    }

    public <T> Long listUnshift(String key, Collection<T> domain) {
        return redisTemplate.opsForList().leftPushAll(key, domain);
    }

    public <T> T listLPop(String key) {
        return (T) redisTemplate.opsForList().leftPop(key);
    }

    public <T> void valuePut(String key, T domain) {
        redisTemplate.opsForValue().set(key, domain);
    }

    public <T> T getValue(String key) {
        return (T) redisTemplate.opsForValue().get(key);
    }

    public boolean hasKey(String key) {
        return BooleanUtils.toBooleanDefaultIfNull(redisTemplate.hasKey(key), false);
    }

    public void remove(String key) {
        redisTemplate.delete(key);
    }

    public boolean expire(String key, long timeout, TimeUnit timeUnit) {
        return redisTemplate.expire(key, timeout, timeUnit);
    }
}
