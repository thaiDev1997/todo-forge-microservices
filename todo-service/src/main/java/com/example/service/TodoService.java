package com.example.service;

import com.example.dto.TodoDTO;

import java.util.List;

public interface TodoService {
    List<TodoDTO> getAll();

    TodoDTO getDetail(long id);

    TodoDTO saveOrUpdate(TodoDTO todo);

    void delete(long id);
}
