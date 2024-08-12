package com.example.controller;

import com.example.client.AccountService;
import com.example.dto.TodoDTO;
import com.example.service.TodoService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/todos")
public class TodoController {

    TodoService todoService;
    AccountService accountService;

    @PreAuthorize("hasAuthority('todo:read')")
    @GetMapping
    public List<TodoDTO> getAll() {
        return todoService.getAll();
    }

    @PreAuthorize("hasAuthority('todo:read') && hasRole('ADMIN')")
    @GetMapping(value = "/{id}")
    public TodoDTO getDetail(@PathVariable(value = "id") @Min(1) long id) {
        return todoService.getDetail(id);
    }

    @PreAuthorize("hasAuthority('todo:write')")
    @PostMapping
    public TodoDTO save(@Valid @RequestBody TodoDTO todo) {
        return todoService.saveOrUpdate(todo);
    }

    @PreAuthorize("hasAuthority('todo:write') && hasRole('ADMIN')")
    @PutMapping(value = "/{id}")
    public TodoDTO update(@PathVariable(value = "id") @Min(1) long id,
                          @RequestBody TodoDTO todo) {
        todo.setId(id);
        return todoService.saveOrUpdate(todo);
    }

    @PreAuthorize("hasAuthority('todo:write') && hasRole('ADMIN')")
    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable(value = "id") @Min(1) long id) {
        todoService.delete(id);
    }

    @GetMapping(value = "/test")
    public String test() {
        return accountService.test();
    }
}
