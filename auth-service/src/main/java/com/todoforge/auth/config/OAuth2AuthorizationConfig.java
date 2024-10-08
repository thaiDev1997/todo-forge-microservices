package com.todoforge.auth.config;

import com.todoforge.auth.service.custom.CustomJwtTokenStore;
import com.todoforge.core.service.RedisService;
import com.todoforge.resource.config.CustomAccessTokenConverter;
import com.todoforge.auth.service.CustomTokenEnhancer;
import com.todoforge.resource.service.SecurityService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.code.RedisAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.util.Arrays;
import java.util.Map;

@SuppressWarnings("deprecation")
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Configuration
@EnableAuthorizationServer
public class OAuth2AuthorizationConfig extends AuthorizationServerConfigurerAdapter {

    final PasswordEncoder passwordEncoder;
    final UserDetailsService userDetailsService;
    final AuthenticationManager authenticationManager;
    final OAuth2ClientProperties oAuth2ClientProperties;
    final CustomTokenEnhancer customTokenEnhancer;
    final CustomAccessTokenConverter customAccessTokenConverter;
    final RedisConnectionFactory redisConnectionFactory;
    final RedisService redisService;
    final SecurityService securityService;

    @Value("${oauth2.key-store.path}")
    String keyStorePath;
    @Value("${oauth2.key-store.password}")
    String keyStorePassword;
    @Value("${oauth2.key-store.alias}")
    String keyStoreAlias;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        Map<String, OAuth2ClientProperties.OAuth2Client> oauth2Clients = oAuth2ClientProperties.getClients();
        int accessTokenValidity = oAuth2ClientProperties.getAccessTokenValidity();
        int refreshTokenValidity = oAuth2ClientProperties.getRefreshTokenValidity();

        InMemoryClientDetailsServiceBuilder inMemoryBuilder = clients.inMemory();
        for (Map.Entry<String, OAuth2ClientProperties.OAuth2Client> oauth2ClientEntry : oauth2Clients.entrySet()) {
            OAuth2ClientProperties.OAuth2Client oauth2Client = oauth2ClientEntry.getValue();
            inMemoryBuilder
                    .withClient(oauth2Client.getClientId())
                    .secret(passwordEncoder.encode(oauth2Client.getClientSecret()))
                    .authorizedGrantTypes(oauth2Client.getGrantTypes())
                    .scopes(oauth2Client.getScopes())
                    .redirectUris(ObjectUtils.defaultIfNull(oauth2Client.getRedirectUris(), new String[]{}))
                    // clients can override general Validity Second
                    .accessTokenValiditySeconds(ObjectUtils.defaultIfNull(oauth2Client.getAccessTokenValidity(), accessTokenValidity))
                    .refreshTokenValiditySeconds(ObjectUtils.defaultIfNull(oauth2Client.getRefreshTokenValidity(), refreshTokenValidity));
        }
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        final TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(customTokenEnhancer, accessTokenConverter()));

        endpoints.authenticationManager(authenticationManager)
                .tokenStore(tokenStore())
                .accessTokenConverter(accessTokenConverter())
                .userDetailsService(userDetailsService)
                .tokenEnhancer(tokenEnhancerChain);
        /* authorizationCode storage that helps to obtain access_token
         * ## In-memory:
         * -> restart a single service => un-known authorization codes gave for Clients before
         * -> hr scalability or availability => Load Balancer: instance 2 doesn't know about
         *   the authorization codes that were generated by instance 1
         * -> InMemoryAuthorizationCodeServices
         */
        // endpoints.authorizationCodeServices(new InMemoryAuthorizationCodeServices()); // In-memory
        /*
         * ## JDBC/Redis (Single truth resource):
         * -> avoid losing the authorization codes on service restarts
         * -> avoid not knowing about the authorization codes of the hr scalability or the availability
         * -> JDBC => JdbcAuthorizationCodeServices
         * -> Redis => RedisAuthorizationCodeServices
         * */
        endpoints.authorizationCodeServices(new RedisAuthorizationCodeServices(redisConnectionFactory)); // Redis
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
        oauthServer.passwordEncoder(passwordEncoder)
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()")
                .allowFormAuthenticationForClients();
    }

    @Primary // use auth-service's bean rather than resource-service
    @Bean
    public TokenStore tokenStore() {
        return new CustomJwtTokenStore(accessTokenConverter(), redisService, securityService);
    }

    @Primary // use auth-service's bean rather than resource-service
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        ClassPathResource classPathResource = new ClassPathResource(keyStorePath);
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(classPathResource, keyStorePassword.toCharArray());

        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setKeyPair(keyStoreKeyFactory.getKeyPair(keyStoreAlias));
        converter.setAccessTokenConverter(customAccessTokenConverter);
        return converter;
    }
}
