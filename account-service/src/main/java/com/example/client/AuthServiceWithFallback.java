package com.example.client;

import feign.RequestInterceptor;
import com.todoforge.resource.interceptor.OAuth2FeignRequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;
import java.util.Map;

@FeignClient(name = "auth-service-fallback", url = "${internal-services.auth-service.url}",
        path = "/oauth", configuration = AuthServiceWithFallbackConfiguration.class,
        // fallback = AuthServiceFallback.class,
        fallbackFactory = AuthServiceFallbackFactory.class)
public interface AuthServiceWithFallback {

    @GetMapping("/me")
    Map<String, Object> me();
}

// @Component
class AuthServiceFallback implements AuthServiceWithFallback {

    @Override
    public Map<String, Object> me() {
        return Collections.EMPTY_MAP;
    }
}

@Slf4j
@Component
class AuthServiceFallbackFactory implements FallbackFactory<AuthServiceWithFallback> {


    @Override
    public AuthServiceWithFallback create(Throwable cause) {
        log.error("Feign fallback: {}", cause.getMessage(), cause);

        return new AuthServiceWithFallback() {
            @Override
            public Map<String, Object> me() {
                return Collections.EMPTY_MAP;
            }
        };
    }
}


class AuthServiceWithFallbackConfiguration {

    @Bean
    public RequestInterceptor oauth2FeignRequestInterceptor() {
        return new OAuth2FeignRequestInterceptor();
    }
}
