package com.todoforge.core.service;

import com.todoforge.core.constant.RedisHashKey;
import com.todoforge.core.dto.UserSessionData;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.Nullable;
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

    public void removeUserSession(String jti) {
        super.hashRemove(jti, RedisHashKey.USER.name());
    }

    public void saveAccessToken(String jti, String accessToken) {
        if (!super.hasKey(jti)) return;
        super.hashPut(jti, RedisHashKey.ACCESS_TOKEN.name(), accessToken);
    }

    @Nullable
    public String getAccessToken(String jti) {
        if (!super.hasKey(jti)) return null;
        return super.hashGet(jti, RedisHashKey.ACCESS_TOKEN.name());
    }

    public void removeAccessToken(String jti) {
        super.hashRemove(jti, RedisHashKey.ACCESS_TOKEN.name());
    }
}
