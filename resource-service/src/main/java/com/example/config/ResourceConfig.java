package com.example.config;

import com.example.aspect.AuthorizeAspect;
import com.example.service.SecurityService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class ResourceConfig {

    @Bean
    public AuthorizeAspect authorizeAspect(SecurityService securityService, Environment environment) {
        return new AuthorizeAspect(securityService, environment);
    }
}
