package com.example.controller;

import com.example.dto.AccountDTO;
import com.example.service.AccountService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@Validated
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
    public AccountDTO getDetail(@PathVariable(value = "id") @Min(1) long id) {
        return accountService.getDetail(id);
    }

    @PostMapping
    public AccountDTO save(@Valid @RequestBody AccountDTO account) {
        return accountService.saveOrUpdate(account);
    }

    @PutMapping(value = "/{id}")
    public AccountDTO update(@PathVariable(value = "id") @Min(1) long id,
                             @Valid @RequestBody AccountDTO account) {
        account.setId(id);
        return accountService.saveOrUpdate(account);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable(value = "id") @Min(1) long id) {
        accountService.delete(id);
    }

    @PostMapping(value = "/{id}/role/{role-id}")
    public void saveRole(@PathVariable(value = "id") @Min(1) long id,
                         @PathVariable(value = "role-id") long roleId) {
        accountService.saveRole(id, roleId);
    }

    @DeleteMapping(value = "/{id}/role/{role-id}")
    public void deleteRole(@PathVariable(value = "id") @Min(1) long id,
                           @PathVariable(value = "role-id") long roleId) {
        accountService.deleteRole(id, roleId);
    }

    @DeleteMapping(value = "/{id}/role")
    public void deleteAllRoles(@PathVariable(value = "id") @Min(1) long id) {
        accountService.deleteAllRoles(id);
    }

    @DeleteMapping(value = "/{id}/profile/{profile-id}")
    public void deleteProfile(@PathVariable(value = "id") @Min(1) long id,
                              @PathVariable(value = "profile-id") long profileId) {
        accountService.deleteProfile(id, profileId);
    }

    @GetMapping(value = "/test")
    public String test() {
        return "Hello!!!";
    }
}
