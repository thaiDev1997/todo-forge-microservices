package com.example;

import com.example.constant.TodoStatus;
import com.example.dto.TodoDTO;
import com.example.entity.TodoEntity;
import com.example.repository.TodoRepository;
import com.example.service.TodoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Slf4j
@EnableCircuitBreaker
@EnableJpaAuditing
@SpringBootApplication
public class TodoServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(TodoServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData(TodoRepository todoRepository, TodoService todoService) {
        return (args) -> {
            log.info("Initialize To Do");
            Page<TodoEntity> allTodos = todoRepository.findAll(PageRequest.of(0, 1));
            if (allTodos.getTotalElements() == 0) {
                TodoDTO todo = TodoDTO.builder().name("To Do").status(TodoStatus.PENDING).build();
                todoService.saveOrUpdate(todo);
            }
        };
    }
}