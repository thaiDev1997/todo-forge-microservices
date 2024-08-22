package com.example.form;

import com.example.constant.ScopePermission;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PermissionForm {
    String title;
    String description;
    ScopePermission scope;
}
