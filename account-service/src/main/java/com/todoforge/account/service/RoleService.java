package com.todoforge.account.service;

import com.todoforge.account.dto.RoleDTO;

import java.util.List;

public interface RoleService {
    List<RoleDTO> getAll();
    RoleDTO getDetail(long id);
    RoleDTO saveOrUpdate(RoleDTO todo);
    void delete(long id);
    void savePermission(long roleId, long permissionId);
    void deletePermission(long roleId, long permissionId);
}
