package com.example.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Configuration
@ConfigurationProperties(prefix = "oauth2.client")
public class OAuth2ClientProperties {
    int accessTokenValidity;
    int refreshTokenValidity;
    Map<String, OAuth2Client> clients;

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class OAuth2Client {
        String clientId;
        String clientSecret;
        String[] grantTypes;
        String[] scopes;
        String[] redirectUris;
        Integer accessTokenValidity;
        Integer refreshTokenValidity;
    }
}
