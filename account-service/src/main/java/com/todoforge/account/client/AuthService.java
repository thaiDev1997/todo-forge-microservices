package com.todoforge.account.client;

import feign.RequestInterceptor;
import com.todoforge.resource.interceptor.OAuth2FeignRequestInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "${internal-services.auth-service.name}", url = "${internal-services.auth-service.url}",
        path = "/oauth",  configuration = AuthServiceConfiguration.class)
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
