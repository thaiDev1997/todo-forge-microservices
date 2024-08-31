package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.converter.JsonMessageConverter;

@Configuration
public class AccountConfig {
    @Bean
    public JsonMessageConverter jsonMessageConverter() {
        return new JsonMessageConverter();
    }
}
