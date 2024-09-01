package com.example.service;

import com.example.constant.RedisHashKey;
import com.example.dto.UserSessionData;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService extends AbstractRedisService {

    public RedisService(RedisTemplate redisTemplate) {
        super(redisTemplate);
    }

    public void saveUserSession(String jti, UserSessionData userSession, long timeout, TimeUnit timeUnit) {
        super.hashPut(jti, RedisHashKey.USER.name(), userSession);
        super.expire(jti, timeout, timeUnit);
    }

    public void updateUserSession(String jti, UserSessionData userSession) {
        super.hashPut(jti, RedisHashKey.USER.name(), userSession);
    }

    public UserSessionData getUserSession(String jti) {
        return super.hashGet(jti, RedisHashKey.USER.name());
    }
}
