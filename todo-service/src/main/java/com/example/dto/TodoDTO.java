package com.example.dto;

import com.example.constant.TodoStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class TodoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    long id;
    String name;
    TodoStatus status;
    LocalDateTime createdAt;
}
