package com.todoforge.todo.service;

import com.todoforge.todo.dto.TodoDTO;

import java.util.List;

public interface TodoService {
    List<TodoDTO> getAll();

    TodoDTO getDetail(long id);

    TodoDTO saveOrUpdate(TodoDTO todo);

    void delete(long id);
}
