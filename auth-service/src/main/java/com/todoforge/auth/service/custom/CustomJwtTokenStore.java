package com.todoforge.auth.service.custom;

import com.todoforge.core.service.RedisService;
import com.todoforge.resource.service.SecurityService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.Objects;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomJwtTokenStore extends JwtTokenStore {
    RedisService redisService;
    SecurityService securityService;

    public CustomJwtTokenStore(JwtAccessTokenConverter jwtTokenEnhancer, RedisService redisService, SecurityService securityService) {
        super(jwtTokenEnhancer);
        this.redisService = redisService;
        this.securityService = securityService;
    }

    @Override
    public void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
        // invoke super method
        super.storeAccessToken(token, authentication);

        Authentication authenticationContext = SecurityContextHolder.getContext().getAuthentication();
        if (securityService.isClientOnly(authentication) && StringUtils.isNotBlank(token.getValue())
                && authenticationContext.isAuthenticated()) {
            String jti = (String) token.getAdditionalInformation().get("jti");
            if (Objects.nonNull(jti)) {
                String accessToken = token.getValue();
                // store access_token into Redis
                redisService.saveAccessToken(jti, accessToken);
            }
        }
    }
}