package com.todoforge.todo.dto;

import com.todoforge.todo.constant.TodoStatus;
import com.todoforge.core.dto.BaseEntityDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class TodoDTO extends BaseEntityDTO {
    private static final long serialVersionUID = 1L;

    @NotBlank
    String name;
    TodoStatus status;

    public TodoDTO(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, String name, TodoStatus status) {
        super(id, createdAt, updatedAt);
        this.name = name;
        this.status = status;
    }

}
