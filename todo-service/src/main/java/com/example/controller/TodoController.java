package com.example.controller;

import com.example.dto.TodoDTO;
import com.example.service.TodoService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/todos")
public class TodoController {

    TodoService todoService;

    @GetMapping
    public List<TodoDTO> getAll() {
        return todoService.getAll();
    }

    @GetMapping(value = "/{id}")
    public TodoDTO getDetail(@PathVariable(value = "id") long id) {
        return todoService.getDetail(id);
    }

    @PostMapping
    public TodoDTO save(@RequestBody TodoDTO todo) {
        return todoService.saveOrUpdate(todo);
    }

    @PutMapping
    public TodoDTO update(@RequestBody TodoDTO todo) {
        return todoService.saveOrUpdate(todo);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable(value = "id") long id) {
        todoService.delete(id);
    }
}
