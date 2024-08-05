package com.example.service.impl;

import com.example.dto.ResourceDTO;
import com.example.entity.PermissionEntity;
import com.example.entity.ResourceEntity;
import com.example.exception.BaseResponseException;
import com.example.form.PermissionForm;
import com.example.repository.PermissionRepository;
import com.example.repository.ResourceRepository;
import com.example.service.ResourceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
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
public class ResourceServiceImpl implements ResourceService {
    @PersistenceContext
    EntityManager entityManager;
    ModelMapper modelMapper;

    ResourceRepository resourceRepository;
    PermissionRepository permissionRepository;

    @Override
    public List<ResourceDTO> getAll(String searchTerm, String sortBy, boolean sortAsc, short page, short size) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ResourceDTO> cq = cb.createQuery(ResourceDTO.class);
        Root<ResourceEntity> root = cq.from(ResourceEntity.class);
        // Set the query's selection to the constructor expression
        cq.select(cb.construct(
                ResourceDTO.class,
                root.get("id"),
                root.get("createdAt"),
                root.get("updatedAt"),
                root.get("title"),
                root.get("code"),
                root.get("description"),
                root.get("isActive")
        ));

        // filtering
        if (StringUtils.isNotBlank(searchTerm)) {
            // build predicates
            String likePattern = "%" + searchTerm + "%";
            Predicate titlePredicate = cb.like(root.get("title"), likePattern);
            Predicate descriptionPredicate = cb.like(root.get("description"), likePattern);

            // combine predicates with OR condition
            Predicate combinedPredicate = cb.or(titlePredicate, descriptionPredicate);
            cq.where(combinedPredicate);
        }

        // sorting
        if (StringUtils.isNotBlank(sortBy)) {
            if (sortAsc) {
                cq.orderBy(cb.asc(root.get(sortBy)));
            } else {
                cq.orderBy(cb.desc(root.get(sortBy)));
            }
        }

        // pagination
        TypedQuery<ResourceDTO> query = entityManager.createQuery(cq);
        query.setFirstResult((page - 1) * size);
        query.setMaxResults(size);

        return query.getResultList();
    }

    @Override
    public ResourceDTO getDetail(long id) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ResourceDTO> cq = cb.createQuery(ResourceDTO.class);
        Root<ResourceEntity> resourceRoot = cq.from(ResourceEntity.class);
        // Set the query's selection to the constructor expression
        cq.select(cb.construct(
                ResourceDTO.class,
                resourceRoot.get("id"),
                resourceRoot.get("createdAt"),
                resourceRoot.get("updatedAt"),
                resourceRoot.get("title"),
                resourceRoot.get("code"),
                resourceRoot.get("description"),
                resourceRoot.get("isActive")
        ));
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(resourceRoot.get("id"), id));
        cq.where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(cq).getSingleResult();
    }

    @Transactional
    @Override
    public ResourceDTO saveOrUpdate(ResourceDTO resource) {
        ResourceEntity resourceEntity;
        Long id = resource.getId();
        String code = resource.getCode();
        if (Objects.isNull(id) || id == 0) {
            resourceEntity = resourceRepository.getActive(code);
            if (Objects.nonNull(resourceEntity)) {
                throw new BaseResponseException(HttpStatus.CONFLICT);
            }
            resourceEntity = modelMapper.map(resource, ResourceEntity.class);
            resourceEntity.setActive(Boolean.TRUE);
            // resourceEntity.setCreatedAt(LocalDateTime.now());
        } else {
            resourceEntity = resourceRepository.getActive(id, code);
            if (Objects.isNull(resourceEntity)) {
                throw new BaseResponseException(HttpStatus.NOT_FOUND);
            }
            resourceEntity.setTitle(resource.getTitle());
            resourceEntity.setDescription(resource.getDescription());
            resourceEntity.setUpdatedAt(LocalDateTime.now());
        }
        resourceRepository.saveAndFlush(resourceEntity);

        modelMapper.map(resourceEntity, resource);
        return resource;
    }

    @Transactional
    @Override
    public void delete(long id) {
        if (id > 0) {
            resourceRepository.deleteById(id);
        }
    }

    @Transactional
    @Override
    public void savePermission(long resourceId, PermissionForm permission) {
        ResourceEntity resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> new BaseResponseException(HttpStatus.NOT_FOUND));
        PermissionEntity permissionEntity = modelMapper.map(permission, PermissionEntity.class);
        permissionEntity.setCreatedAt(LocalDateTime.now());
        boolean result = resource.addPermission(permissionEntity);
        if (!result && CollectionUtils.isNotEmpty(resource.getPermissions())) {
            // permission already saves
            throw new BaseResponseException(HttpStatus.CONFLICT);
        }
        resourceRepository.save(resource);
    }

    @Transactional
    @Override
    public void deletePermission(long resourceId, long permissionId) {
        PermissionEntity permission = permissionRepository.findById(permissionId).orElseThrow(() -> new BaseResponseException(HttpStatus.NOT_FOUND));
        ResourceEntity resource = permission.getResource();
        if (Objects.isNull(resource)) {
            throw new BaseResponseException(HttpStatus.NOT_FOUND);
        }
        if (resourceId != resource.getId()) {
            throw new BaseResponseException(HttpStatus.BAD_REQUEST);
        }
        permissionRepository.delete(permission);
    }
}
