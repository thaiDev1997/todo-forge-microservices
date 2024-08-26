package com.example.service;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.stereotype.Service;

@SuppressWarnings("deprecation")
@Service
public class SecurityService {

    private String getClientId(Authentication authentication) {
        OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) authentication;
        OAuth2Request oAuth2Request = oAuth2Authentication.getOAuth2Request();
        return oAuth2Request.getClientId();
    }

    public boolean hasClientId(Authentication authentication, String clientId) {
        return StringUtils.isNotBlank(clientId) && clientId.equals(this.getClientId(authentication));
    }

    public boolean hasClientId(String clientId) {
        return this.hasClientId(SecurityContextHolder.getContext().getAuthentication(), clientId);
    }

    public boolean hasClientId(Authentication authentication, String... clientIds) {
        return ArrayUtils.isNotEmpty(clientIds) && StringUtils.equalsAny(this.getClientId(authentication), clientIds);
    }

    public boolean hasClientId(String... clientIds) {
        return this.hasClientId(SecurityContextHolder.getContext().getAuthentication(), clientIds);
    }
}
