package com.example.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class BaseDTO implements Serializable {
    Long id;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
