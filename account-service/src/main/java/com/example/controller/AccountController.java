package com.example.controller;

import com.example.dto.AccountDTO;
import com.example.service.AccountService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/accounts")
public class AccountController {

    AccountService accountService;

    @GetMapping
    public List<AccountDTO> getAll() {
        return accountService.getAll();
    }

    @GetMapping(value = "/{id}")
    public AccountDTO getDetail(@PathVariable(value = "id") long id) {
        return accountService.getDetail(id);
    }

    @PostMapping
    public AccountDTO save(@RequestBody AccountDTO account) {
        return accountService.saveOrUpdate(account);
    }

    @PutMapping
    public AccountDTO update(@RequestBody AccountDTO account) {
        return accountService.saveOrUpdate(account);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable(value = "id") long id) {
        accountService.delete(id);
    }

}
