package com.todoforge.auth.service;

import com.todoforge.auth.dto.AccountPermissionDTO;
import com.todoforge.auth.dto.TodoUser;
import com.todoforge.core.dto.UserSessionData;
import com.todoforge.core.service.RedisService;
import com.todoforge.resource.dto.AccountPayload;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("deprecation")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class CustomTokenEnhancer implements TokenEnhancer {

    RedisService redisService;

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication oAuth2Authentication) {
        Object principal = Objects.nonNull(oAuth2Authentication.getUserAuthentication())
                ? oAuth2Authentication.getUserAuthentication().getPrincipal() : null;
        if (Objects.nonNull(principal) && principal instanceof TodoUser) {
            TodoUser todoUser = (TodoUser) principal;
            AccountPermissionDTO.AccountRoleDTO accountRole = todoUser.getAccountPermission().getAccount();
            String jti = oAuth2AccessToken.getValue();
            // build UserSession object
            UserSessionData userSessionData = UserSessionData.builder()
                    .jti(jti)
                    .username(todoUser.getUsername())
                    .firstName(todoUser.getFirstName())
                    .lastName(todoUser.getLastName())
                    .emailAddress(todoUser.getEmailAddress())
                    .lastLogin(accountRole.getLastLogin())
                    .roles(accountRole.getRoles())
                    .build();
            // TTL of UserSessionData is equal to TTL of access_token
            long diffInMillies = Math.abs(oAuth2AccessToken.getExpiration().getTime() - new Date().getTime());
            long diff = diffInMillies / 1000;
            // save UserSession into Redis
            redisService.saveUserSession(jti, userSessionData, diff, TimeUnit.SECONDS);

            if (oAuth2AccessToken instanceof DefaultOAuth2AccessToken) {
                DefaultOAuth2AccessToken defaultOAuth2AccessToken = (DefaultOAuth2AccessToken) oAuth2AccessToken;
                // put more data into payload
                final int numberOfAdditionalPayload = 1;
                Map<String, Object> additionalInfo = new HashMap<>(numberOfAdditionalPayload);
                AccountPayload accountPayload = AccountPayload.builder()
                        .firstName(todoUser.getFirstName())
                        .lastName(todoUser.getLastName())
                        .emailAddress(todoUser.getEmailAddress())
                        .build();
                additionalInfo.put(AccountPayload.PAYLOAD_NAME, accountPayload);

                // assign additional data into payload
                defaultOAuth2AccessToken.setAdditionalInformation(additionalInfo);

                OAuth2Request oAuth2Request = oAuth2Authentication.getOAuth2Request();
                String grantType = oAuth2Request.getGrantType();
                boolean isRefreshToken = Objects.isNull(grantType) || "refresh_token".equals(grantType);
                if (isRefreshToken) {
                    // grant type is refresh_token -> instead, use the original refresh token
                    // it doesn't return a new refresh token.
                    defaultOAuth2AccessToken.setRefreshToken(null);
                }
            }
        }
        return oAuth2AccessToken;
    }
}
