package com.example.service.impl;

import com.example.dto.TodoDTO;
import com.example.entity.TodoEntity;
import com.example.repository.TodoRepository;
import com.example.service.TodoService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {
    TodoRepository todoRepository;
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<TodoDTO> getAll() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<TodoDTO> cq = cb.createQuery(TodoDTO.class);
        Root<TodoEntity> todoRoot = cq.from(TodoEntity.class);
        // Set the query's selection to the constructor expression
        cq.select(cb.construct(
                TodoDTO.class,
                todoRoot.get("id"),
                todoRoot.get("name"),
                todoRoot.get("status"),
                todoRoot.get("createdAt")
        ));
        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    public TodoDTO getDetail(long id) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<TodoDTO> cq = cb.createQuery(TodoDTO.class);
        Root<TodoEntity> todoRoot = cq.from(TodoEntity.class);
        // Set the query's selection to the constructor expression
        cq.select(cb.construct(
                TodoDTO.class,
                todoRoot.get("id"),
                todoRoot.get("name"),
                todoRoot.get("status"),
                todoRoot.get("createdAt")
        ));
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(todoRoot.get("id"), id));
        cq.where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(cq).getSingleResult();
    }

    @Transactional
    @Override
    public TodoDTO saveOrUpdate(TodoDTO todo) {
        long id = todo.getId();
        TodoEntity todoEntity;
        if (id == 0) {
            todoEntity = new TodoEntity();
            todoEntity.setCreatedAt(LocalDateTime.now());
        } else {
            todoEntity = todoRepository.getReferenceById(id);
            todoEntity.setStatus(todo.getStatus());

        }
        todoEntity.setName(todo.getName());
        todo.setId(todo.getId());
        todoRepository.save(todoEntity);
        return todo;
    }

    @Transactional
    @Override
    public void delete(long id) {
        if (id > 0) {
            todoRepository.deleteById(id);
        }
    }
}
