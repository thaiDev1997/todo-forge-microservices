package com.example.client;

import feign.RequestInterceptor;
import interceptor.OAuth2FeignRequestInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "${internal-services.auth-service.name}", url = "${internal-services.gateway-service.url}",
        path = "/auth/api/v1/auth",  configuration = AuthServiceConfiguration.class)
public interface AuthService {

    @GetMapping("/greeting")
    String greeting();
}

class AuthServiceConfiguration {

    @Bean
    public RequestInterceptor oauth2FeignRequestInterceptor() {
        return new OAuth2FeignRequestInterceptor();
    }
}
