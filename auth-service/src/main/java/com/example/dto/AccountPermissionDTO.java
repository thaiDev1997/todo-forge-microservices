package com.example.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
@Setter
@RequiredArgsConstructor
public class AccountPermissionDTO {
    AccountRoleDTO account;
    List<ResourcePermission> permissions;

    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Setter
    @Getter
    public static class AccountRoleDTO extends AccountDTO {
        List<String> roles;
    }

    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    @Getter
    @Setter
    @RequiredArgsConstructor
    public static class ResourcePermission {
        String resourceCode;
        String scope;
    }
}
