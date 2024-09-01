package com.todoforge.account.service;

import com.todoforge.account.dto.ResourceDTO;
import com.todoforge.account.form.PermissionForm;

import java.util.List;

public interface ResourceService {
    List<ResourceDTO> getAll(String searchTerm, String sortBy, boolean sortAsc, short page, short size);
    ResourceDTO getDetail(long id);
    ResourceDTO saveOrUpdate(ResourceDTO todo);
    void delete(long id);
    Long savePermission(long resourceId, PermissionForm permission);
    void deletePermission(long resourceId, long permissionId);
}
