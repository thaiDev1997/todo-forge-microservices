package com.todoforge.todo.controller;

import com.todoforge.todo.client.AccountService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/app")
public class ApplicationInfoController {

    final AccountService accountService;

    @Value("${spring.application.name}")
    private String applicationName;

    @GetMapping("/name")
    public String getName() {
        return applicationName;
    }

    @GetMapping(value = "/account-name")
    public String getAccountApplicationName() {
        return accountService.getName();
    }

}