package com.todoforge.resource.config;

import com.todoforge.resource.dto.AccountPayload;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("deprecation")
@Component
public class CustomAccessTokenConverter extends DefaultAccessTokenConverter {

    // put more data into Authentication.details
    @Override
    public OAuth2Authentication extractAuthentication(Map<String, ?> map) {
        Map<String, ?> appliedMap = map;
        if (map.containsKey(AccountPayload.PAYLOAD_NAME)) {
            Map<String, Object> customMap = new HashMap<>(map);
            // convert Map into AccountPayload class
            ObjectMapper mapper = new ObjectMapper();
            AccountPayload accountPayload = mapper.convertValue(customMap.get(AccountPayload.PAYLOAD_NAME), AccountPayload.class);
            customMap.replace(AccountPayload.PAYLOAD_NAME, accountPayload);
            appliedMap = customMap;
        }
        OAuth2Authentication authentication = super.extractAuthentication(appliedMap);
        authentication.setDetails(appliedMap);
        return authentication;
    }
}
