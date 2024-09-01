package com.todoforge.todo.entity;

import com.todoforge.todo.constant.TodoStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "todo")
@Getter
@Setter
public class TodoEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private TodoStatus status = TodoStatus.PENDING;
}
