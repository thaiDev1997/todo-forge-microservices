package com.todoforge.todo.controller;

import com.todoforge.todo.client.AuthService;
import com.todoforge.todo.client.AuthServiceWithFallback;
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
@RequestMapping(value = "/oauth")
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
