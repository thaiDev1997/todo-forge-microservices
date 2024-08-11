package com.example.dto;

import com.example.constant.AccountStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountDTO extends BaseEntityDTO {
    private static final long serialVersionUID = 1L;

    String username;
    String password;
    AccountStatus status;
    LocalDateTime lastLogin;
    ProfileDTO profile;

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class ProfileDTO implements Serializable {
        private static final long serialVersionUID = 1L;

        String firstName;
        String lastName;
        String emailAddress;
    }
}
