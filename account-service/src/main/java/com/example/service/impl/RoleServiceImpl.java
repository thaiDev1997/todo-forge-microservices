package com.example.service.impl;

import com.example.dto.RoleDTO;
import com.example.entity.PermissionEntity;
import com.example.entity.RoleEntity;
import com.example.exception.BaseResponseException;
import com.example.repository.PermissionRepository;
import com.example.repository.RoleRepository;
import com.example.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.collections4.CollectionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    @PersistenceContext
    EntityManager entityManager;
    ModelMapper modelMapper;

    RoleRepository roleRepository;
    PermissionRepository permissionRepository;

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
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
        try {
            return entityManager.createQuery(cq).getSingleResult();
        } catch (NoResultException noResultException) {
            return null;
        }
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
        } else {
            roleEntity = roleRepository.getActive(id, code);
            if (Objects.isNull(roleEntity)) {
                throw new BaseResponseException(HttpStatus.NOT_FOUND);
            }
            roleEntity.setTitle(role.getTitle());
            roleEntity.setDescription(role.getDescription());
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

    @Transactional
    @Override
    public void savePermission(long roleId, long permissionId) {
        RoleEntity roleEntity = roleRepository.findById(roleId)
                .orElseThrow(() -> new BaseResponseException(HttpStatus.NOT_FOUND));
        PermissionEntity permissionEntity = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new BaseResponseException(HttpStatus.NOT_FOUND));
        boolean result = roleEntity.addPermission(permissionEntity);
        if (!result) throw new BaseResponseException(HttpStatus.BAD_REQUEST);

        roleRepository.save(roleEntity);
    }

    @Transactional
    @Override
    public void deletePermission(long roleId, long permissionId) {
        RoleEntity roleEntity = roleRepository.findById(roleId)
                .orElseThrow(() -> new BaseResponseException(HttpStatus.NOT_FOUND));
        Set<PermissionEntity> permissions = roleEntity.getPermissions();
        if (CollectionUtils.isEmpty(permissions)) return;
        PermissionEntity permissionEntity = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new BaseResponseException(HttpStatus.NOT_FOUND));
        boolean result = permissions.remove(permissionEntity);
        if (!result) throw new BaseResponseException(HttpStatus.BAD_REQUEST);
        roleRepository.save(roleEntity);
        // doesn't need this: permissionEntity.getRoles().remove(roleEntity);
    }
}
