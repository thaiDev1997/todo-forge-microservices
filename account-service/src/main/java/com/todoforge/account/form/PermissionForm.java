package com.todoforge.account.form;

import com.todoforge.account.constant.ScopePermission;
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
