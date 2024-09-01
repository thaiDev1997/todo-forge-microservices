package com.example.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
@Setter
@RequiredArgsConstructor
public class AccountPermissionDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    AccountRoleDTO account;
    List<ResourcePermission> permissions;

    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Setter
    @Getter
    public static class AccountRoleDTO extends AccountDTO {
        private static final long serialVersionUID = 1L;

        List<String> roles;
    }

    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    @Getter
    @Setter
    @RequiredArgsConstructor
    public static class ResourcePermission implements Serializable {
        private static final long serialVersionUID = 1L;

        String resourceCode;
        String scope;
    }
}
