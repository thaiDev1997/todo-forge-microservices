package com.todoforge.todo;

import com.todoforge.todo.constant.TodoStatus;
import com.todoforge.todo.dto.TodoDTO;
import com.todoforge.todo.entity.TodoEntity;
import com.todoforge.todo.repository.TodoRepository;
import com.todoforge.todo.service.TodoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableDiscoveryClient
@EnableFeignClients
@Slf4j
@EnableCircuitBreaker
@EnableJpaAuditing
@SpringBootApplication(scanBasePackages = {
        "com.todoforge.core",
        "com.todoforge.resource",
        "com.todoforge.todo"
})
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