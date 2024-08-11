package com.example.client;

import com.example.dto.AccountPermissionDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "account-service", url = "http://localhost:8085", path = "/api/v1/accounts")
public interface AccountService {

    @GetMapping(value = "/username/{username}")
    AccountPermissionDTO getByUsername(@PathVariable(value = "username") String username);
}
