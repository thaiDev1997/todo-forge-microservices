package com.todoforge.resource.config;

import com.todoforge.resource.aspect.AuthorizeAspect;
import com.todoforge.resource.interceptor.VerifyUserSessionInterceptor;
import com.todoforge.resource.service.SecurityService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@SuppressWarnings("deprecation")
@Configuration
public class ResourceConfig implements WebMvcConfigurer {

    CustomAccessTokenConverter customAccessTokenConverter;
    VerifyUserSessionInterceptor verifyUserSessionInterceptor;

    @ConditionalOnMissingBean(TokenStore.class)
    @Bean("tokenStoreResource")
    public TokenStore tokenStore() throws IOException {
        return new JwtTokenStore(accessTokenConverter());
    }

    @ConditionalOnMissingBean(JwtAccessTokenConverter.class)
    @Bean("accessTokenConverterResource")
    public JwtAccessTokenConverter accessTokenConverter() throws IOException {
        Resource resource = new ClassPathResource("todo-forge-public.txt"); // generated by openssl-for-windows google
        String publicKey = new BufferedReader(new InputStreamReader(resource.getInputStream())).lines()
                .collect(Collectors.joining("\n"));
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setVerifierKey(publicKey);
        converter.setAccessTokenConverter(customAccessTokenConverter);
        return converter;
    }

    @Bean
    public AuthorizeAspect authorizeAspect(SecurityService securityService, Environment environment) {
        return new AuthorizeAspect(securityService, environment);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        final String[] authenticationPatternUrls = new String[]{
                "/oauth/token", "/oauth/authorize", "/oauth/logout"
        };
        registry.addInterceptor(verifyUserSessionInterceptor)
                .order(Ordered.HIGHEST_PRECEDENCE)
                .excludePathPatterns(authenticationPatternUrls);
    }
}
