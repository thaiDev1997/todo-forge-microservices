package com.example.controller;

import com.example.dto.AccountPayload;
import com.example.dto.UserSessionData;
import com.example.exception.BaseResponseException;
import com.example.service.RedisService;
import com.example.service.SecurityService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;
import java.util.Objects;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/oauth")
public class AuthController {

    RedisService redisService;
    SecurityService securityService;

    @PreAuthorize("#oauth2.hasScope('read') && isAuthenticated()")
    @GetMapping("/me")
    public Principal me(Principal principal) {
        return principal;
    }

    @PreAuthorize("#oauth2.hasScope('read') && isAuthenticated()")
    @GetMapping("/greeting")
    public String greeting(Principal principal) {
        return String.format("Hello, [%s]! Hope you're having a wonderful day!", principal.getName());
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/logout")
    public void logout() {
        if (!securityService.isClientOnly()) {
            String jti = securityService.getJti();
            if (Objects.nonNull(jti)) {
                redisService.remove(jti);
            }
        }
    }
}
