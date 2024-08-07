package com.example.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "account-service", url = "http://localhost:8085", path = "/api/v1")
public interface AccountService {

    @GetMapping(value = "/accounts/test")
    String test();
}
