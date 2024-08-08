package com.example.dto;

import com.example.constant.AccountStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    @NotBlank
    String username;
    @NotBlank
    String password;
    @NotNull
    AccountStatus status;
    LocalDateTime lastLogin;
    ProfileDTO profile;

    public AccountDTO(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, String username, String password,
                      AccountStatus status, LocalDateTime lastLogin) {
        super(id, createdAt, updatedAt);
        this.username = username;
        this.password = password;
        this.status = status;
        this.lastLogin = lastLogin;
    }

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProfileDTO implements Serializable {
        private static final long serialVersionUID = 1L;

        String firstName;
        String lastName;
        String emailAddress;
    }
}
