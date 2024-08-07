package com.example.dto;

import com.example.constant.AccountStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    String username;
    String password;
    AccountStatus status;
    LocalDateTime lastLogin;
    // TODO: profile information

    public AccountDTO(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, String username, String password,
                      AccountStatus status, LocalDateTime lastLogin) {
        super(id, createdAt, updatedAt);
        this.username = username;
        this.password = password;
        this.status = status;
        this.lastLogin = lastLogin;
    }
}
