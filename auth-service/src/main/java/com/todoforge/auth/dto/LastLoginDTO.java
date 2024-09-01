package com.todoforge.auth.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LastLoginDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    String username;
    LocalDateTime localDateTime;
}
