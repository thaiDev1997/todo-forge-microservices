package com.example.dto;

import com.example.constant.TodoStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class TodoDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    String name;
    TodoStatus status;

    public TodoDTO(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, String name, TodoStatus status) {
        super(id, createdAt, updatedAt);
        this.name = name;
        this.status = status;
    }

}
