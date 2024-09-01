package com.todoforge.account.constant;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Getter
public enum ScopePermission {
    READ("read"),
    WRITE("write");

    String name;

    public static ScopePermission fromName(String name) {
        for (ScopePermission scopePermission : ScopePermission.values()) {
            if (scopePermission.getName().equals(name)) {
                return scopePermission;
            }
        }
        throw new IllegalArgumentException("Unknown name: " + name);
    }
}
