package com.todoforge.account.controller;

import com.todoforge.account.dto.RoleDTO;
import com.todoforge.account.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@Validated
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
    public RoleDTO getDetail(@PathVariable(value = "id") @Min(1) long id) {
        return roleService.getDetail(id);
    }

    @PostMapping
    public RoleDTO save(@Valid @RequestBody RoleDTO role) {
        return roleService.saveOrUpdate(role);
    }

    @PutMapping(value = "/{id}")
    public RoleDTO update(@PathVariable(value = "id") @Min(1) long id,
                          @RequestBody RoleDTO role) {
        role.setId(id);
        return roleService.saveOrUpdate(role);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable(value = "id") @Min(1) long id) {
        roleService.delete(id);
    }

    @PostMapping(value = "/{id}/permissions/{permission-id}")
    public void savePermission(@PathVariable(value = "id") @Min(1) long roleId,
                               @PathVariable(value = "permission-id") @Min(1) long permissionId) {
        roleService.savePermission(roleId, permissionId);
    }

    @DeleteMapping(value = "/{id}/permissions/{permission-id}")
    public void deletePermission(@PathVariable(value = "id") @Min(1) long roleId,
                                 @PathVariable(value = "permission-id") @Min(1) long permissionId) {
        roleService.deletePermission(roleId, permissionId);
    }
}
