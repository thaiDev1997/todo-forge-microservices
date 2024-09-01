package com.example.controller;

import com.todoforge.resource.aspect.annotation.VerifyClientId;
import com.example.dto.AccountDTO;
import com.example.dto.AccountPermissionDTO;
import com.example.service.AccountService;
import com.todoforge.resource.service.SecurityService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/accounts")
public class AccountController {

    AccountService accountService;
    SecurityService securityService;

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

    @VerifyClientId("${internal-services.account-service.name}") // only for "account-service" client
    @GetMapping(value = "/username/{username}")
    public AccountPermissionDTO getByUsername(@PathVariable(value = "username") @NotBlank String username) {
        return accountService.getByUsername(username);
    }

}
