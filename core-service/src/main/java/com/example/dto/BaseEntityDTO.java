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
public class BaseEntityDTO implements Serializable {
    Long id = 0L;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
