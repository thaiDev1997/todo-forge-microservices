package com.example.service.impl;

import com.example.dto.RoleDTO;
import com.example.entity.RoleEntity;
import com.example.entity.RoleEntity;
import com.example.exception.BaseResponseException;
import com.example.repository.RoleRepository;
import com.example.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
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
import java.util.Objects;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    @PersistenceContext
    EntityManager entityManager;
    RoleRepository roleRepository;
    ModelMapper modelMapper;

    @Override
    public List<RoleDTO> getAll() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<RoleDTO> cq = cb.createQuery(RoleDTO.class);
        Root<RoleEntity> roleRoot = cq.from(RoleEntity.class);
        // Set the query's selection to the constructor expression
        cq.select(cb.construct(
                RoleDTO.class,
                roleRoot.get("id"),
                roleRoot.get("createdAt"),
                roleRoot.get("updatedAt"),
                roleRoot.get("title"),
                roleRoot.get("code"),
                roleRoot.get("description"),
                roleRoot.get("isActive")
        ));
        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    public RoleDTO getDetail(long id) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<RoleDTO> cq = cb.createQuery(RoleDTO.class);
        Root<RoleEntity> roleRoot = cq.from(RoleEntity.class);
        // Set the query's selection to the constructor expression
        cq.select(cb.construct(
                RoleDTO.class,
                roleRoot.get("id"),
                roleRoot.get("createdAt"),
                roleRoot.get("updatedAt"),
                roleRoot.get("title"),
                roleRoot.get("code"),
                roleRoot.get("description"),
                roleRoot.get("isActive")
        ));
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(roleRoot.get("id"), id));
        cq.where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(cq).getSingleResult();
    }

    @Transactional
    @Override
    public RoleDTO saveOrUpdate(RoleDTO role) {
        RoleEntity roleEntity;
        long id = role.getId();
        String code = role.getCode();
        if (id == 0) {
            roleEntity = roleRepository.getActive(code);
            if (Objects.nonNull(roleEntity)) {
                throw new BaseResponseException(HttpStatus.CONFLICT);
            }
            roleEntity = modelMapper.map(role, RoleEntity.class);
            roleEntity.setActive(Boolean.TRUE);
            roleEntity.setCreatedAt(LocalDateTime.now());
        } else {
            roleEntity = roleRepository.getActive(id, code);
            if (Objects.isNull(roleEntity)) {
                throw new BaseResponseException(HttpStatus.NOT_FOUND);
            }
            roleEntity.setTitle(role.getTitle());
            roleEntity.setDescription(role.getDescription());
            roleEntity.setUpdatedAt(LocalDateTime.now());
        }
        roleRepository.saveAndFlush(roleEntity);

        modelMapper.map(roleEntity, role);
        return role;
    }

    @Transactional
    @Override
    public void delete(long id) {
        if (id > 0) {
            roleRepository.deleteById(id);
        }
    }
}
