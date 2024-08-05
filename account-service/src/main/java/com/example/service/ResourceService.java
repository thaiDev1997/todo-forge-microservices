package com.example.service;

import com.example.dto.ResourceDTO;
import com.example.form.PermissionForm;

import java.util.List;

public interface ResourceService {
    List<ResourceDTO> getAll(String searchTerm, String sortBy, boolean sortAsc, short page, short size);
    ResourceDTO getDetail(long id);
    ResourceDTO saveOrUpdate(ResourceDTO todo);
    void delete(long id);
    void savePermission(long resourceId, PermissionForm permission);
    void deletePermission(long resourceId, long permissionId);
}
