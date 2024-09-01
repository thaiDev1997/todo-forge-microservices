package com.example.interceptor;

import com.example.dto.UserSessionData;
import com.example.exception.BaseResponseException;
import com.example.service.RedisService;
import com.example.service.SecurityService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Component
public class VerifyUserSessionInterceptor implements HandlerInterceptor {

    SecurityService securityService;
    RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!RequestMethod.OPTIONS.name().equalsIgnoreCase(request.getMethod())) {
            if (!securityService.isClientOnly()) {
                UserSessionData userSession = securityService.getCurrentSession();
                if (Objects.isNull(userSession)) throw new BaseResponseException(
                        "Authorization isn't available anymore", HttpStatus.UNAUTHORIZED);
            }
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
