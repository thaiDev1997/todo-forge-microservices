package com.todoforge.account.dto;

import com.todoforge.account.constant.AccountStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
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

        public AccountRoleDTO(Long id, String username, String password, AccountStatus status, LocalDateTime lastLogin,
                              String firstName, String lastName, String emailAddress) {
            super(id, username, password, status, lastLogin, new ProfileDTO(firstName, lastName, emailAddress));
        }
    }

    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    @Getter
    @Setter
    @RequiredArgsConstructor
    public static class ResourcePermission {
        public static final String GET_RESOURCE_PERMISSION_MAPPING = "ResourcePermissionResult";

        String resourceCode;
        String scope;
    }
}
