package com.todoforge.auth.client;

import com.todoforge.auth.dto.AccountPermissionDTO;
import com.todoforge.core.dto.InternalClientCredentials;
import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.security.OAuth2FeignRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "${internal-services.account-service.name}", url = "${internal-services.account-service.url}",
        path = "/api/v1", configuration = AccountFeignClientConfiguration.class)
public interface AccountService {

    @GetMapping(value = "/accounts/username/{username}")
    AccountPermissionDTO getByUsername(@PathVariable(value = "username") String username);

    @GetMapping(value = "/app/name")
    String getName();
}

@SuppressWarnings("deprecation")
@EnableConfigurationProperties
class AccountFeignClientConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "oauth2.internal-client-credentials.account-service")
    public InternalClientCredentials internalClientCredentials() {
        return new InternalClientCredentials();
    }

    @Bean
    public RequestInterceptor requestInterceptor(@Value(value = "${oauth2.access-token-uri}") String accessTokenUri,
                                                 InternalClientCredentials internalClientCredentials) {
        final ClientCredentialsResourceDetails details = new ClientCredentialsResourceDetails();
        details.setAccessTokenUri(accessTokenUri);
        details.setClientId(internalClientCredentials.getClientId());
        details.setClientSecret(internalClientCredentials.getClientSecret());
        return new OAuth2FeignRequestInterceptor(new DefaultOAuth2ClientContext(), details);
    }
}
