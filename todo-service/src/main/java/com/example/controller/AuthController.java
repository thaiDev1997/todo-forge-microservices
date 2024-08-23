package com.example.controller;

import com.example.client.AuthService;
import com.example.client.AuthServiceWithFallback;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/auth")
public class AuthController {

    AuthServiceWithFallback authServiceWithFallback;
    AuthService authService;

    @GetMapping("/me")
    public Map<String, Object> me() {
        return authServiceWithFallback.me();
    }

    @GetMapping("/greeting")
    public String greeting() {
        return authService.greeting();
    }

}
