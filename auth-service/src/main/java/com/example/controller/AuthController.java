package com.example.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping(value = "/api/v1/auth")
public class AuthController {

    @PreAuthorize("#oauth2.hasScope('read') && isAuthenticated()")
    @GetMapping("/me")
    public Principal me(Principal principal) {
        return principal;
    }

}
