package com.example.form;

import com.example.constant.ScopePermission;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PermissionForm {
    String title;
    String description;
    ScopePermission scope;
}
