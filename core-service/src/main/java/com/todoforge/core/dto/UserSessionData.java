package com.todoforge.core.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@ToString
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSessionData implements Serializable {
    private static final long serialVersionUID = 1L;

    String jti; // json token identifier
    String username;
    String firstName;
    String lastName;
    String emailAddress;
    LocalDateTime lastLogin;
    List<String> roles;
}
