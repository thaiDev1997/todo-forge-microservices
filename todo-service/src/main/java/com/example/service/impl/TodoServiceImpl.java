package com.example.service.impl;

import com.example.dto.TodoDTO;
import com.example.entity.TodoEntity;
import com.example.repository.TodoRepository;
import com.example.service.TodoService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {
    TodoRepository todoRepository;
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<TodoDTO> getAll() {
        log.info("Todo - getAll");
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<TodoDTO> cq = cb.createQuery(TodoDTO.class);
        Root<TodoEntity> todoRoot = cq.from(TodoEntity.class);
        // Set the query's selection to the constructor expression
        cq.select(cb.construct(
                TodoDTO.class,
                todoRoot.get("id"),
                todoRoot.get("createdAt"),
                todoRoot.get("updatedAt"),
                todoRoot.get("name"),
                todoRoot.get("status")
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
                todoRoot.get("createdAt"),
                todoRoot.get("updatedAt"),
                todoRoot.get("name"),
                todoRoot.get("status")
        ));
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(todoRoot.get("id"), id));
        cq.where(predicates.toArray(new Predicate[0]));
        try {
            return entityManager.createQuery(cq).getSingleResult();
        } catch (NoResultException noResultException) {
            return null;
        }
    }

    @Transactional
    @Override
    public TodoDTO saveOrUpdate(TodoDTO todo) {
        long id = todo.getId();
        TodoEntity todoEntity;
        if (id == 0) {
            todoEntity = new TodoEntity();
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
