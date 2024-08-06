package com.example.controller;

import com.example.dto.RoleDTO;
import com.example.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/roles")
public class RoleController {

    RoleService roleService;

    @GetMapping
    public List<RoleDTO> getAll() {
        return roleService.getAll();
    }

    @GetMapping(value = "/{id}")
    public RoleDTO getDetail(@PathVariable(value = "id") long id) {
        return roleService.getDetail(id);
    }

    @PostMapping
    public RoleDTO save(@RequestBody RoleDTO role) {
        return roleService.saveOrUpdate(role);
    }

    @PutMapping
    public RoleDTO update(@RequestBody RoleDTO role) {
        return roleService.saveOrUpdate(role);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable(value = "id") long id) {
        roleService.delete(id);
    }

    @PostMapping(value = "/{id}/permissions/{permission-id}")
    public void savePermission(@PathVariable(value = "id") long roleId,
                               @PathVariable(value = "permission-id") long permissionId) {
        roleService.savePermission(roleId, permissionId);
    }

    @DeleteMapping(value = "/{id}/permissions/{permission-id}")
    public void deletePermission(@PathVariable(value = "id") long roleId,
                                 @PathVariable(value = "permission-id") long permissionId) {
        roleService.deletePermission(roleId, permissionId);
    }
}
