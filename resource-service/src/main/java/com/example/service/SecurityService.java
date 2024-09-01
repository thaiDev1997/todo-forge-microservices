package com.example.service;

import com.example.dto.UserSessionData;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@SuppressWarnings("deprecation")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class SecurityService {

    RedisService redisService;

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

    public Map getDecodedDetails(Authentication authentication) {
        Map decodedDetails;
        if (authentication instanceof OAuth2Authentication && authentication.getDetails() instanceof Map) {
            decodedDetails = (Map) authentication.getDetails();
        } else {
            OAuth2AuthenticationDetails oAuth2AuthenticationDetails = getAuthenticationDetail(authentication);
            decodedDetails = (Map) oAuth2AuthenticationDetails.getDecodedDetails();
        }
        return decodedDetails;
    }

    public OAuth2AuthenticationDetails getAuthenticationDetail(Authentication authentication) {
        OAuth2AuthenticationDetails oauthsDetails = null;
        if (authentication instanceof OAuth2Authentication) {
            Object authenticationDetails = authentication.getDetails();
            if (authenticationDetails instanceof OAuth2AuthenticationDetails) {
                oauthsDetails = (OAuth2AuthenticationDetails) authenticationDetails;
            }
        }
        if (Objects.isNull(oauthsDetails)) {
            String oAuthExMsg = "Can not get current OAuth2AuthenticationDetails";
            throw new AuthenticationCredentialsNotFoundException(oAuthExMsg);
        }
        return oauthsDetails;
    }

    public boolean isClientOnly() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return isClientOnly(authentication);
    }

    public boolean isClientOnly(Authentication authentication) {
        if (authentication instanceof OAuth2Authentication) {
            // user belongs to client credential?
            return ((OAuth2Authentication) authentication).isClientOnly();
        }
        return false;
    }

    public String getJti() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return getJti(authentication);
    }

    public String getJti(Authentication authentication) {
        return (String) getDecodedDetails(authentication).get("jti");
    }

    public UserSessionData getCurrentSession() {
        String jti = this.getJti();
        if (Objects.nonNull(jti)) return redisService.getUserSession(jti);
        return null;
    }
}
