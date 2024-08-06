package com.example.controller;

import com.example.dto.ResourceDTO;
import com.example.form.PermissionForm;
import com.example.service.ResourceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/resources") // Base Path
public class ResourceController {

    ResourceService resourceService;

    @GetMapping
    public List<ResourceDTO> getAll(@RequestParam(value = "filter", required = false) String filter,
                                    @RequestParam(value = "sortBy", required = false) String sortBy,
                                    @RequestParam(value = "sortAsc", defaultValue = "true") boolean sortAsc,
                                    @RequestParam(value = "page", defaultValue = "1") short page,
                                    @RequestParam(value = "size", defaultValue = "10") short size) {
        return resourceService.getAll(filter, sortBy, sortAsc, page, size);
    }

    @GetMapping(value = "/{id}")
    public ResourceDTO getDetail(@PathVariable(value = "id") long id) {
        return resourceService.getDetail(id);
    }

    @PostMapping
    public ResourceDTO save(@RequestBody ResourceDTO resource) {
        return resourceService.saveOrUpdate(resource);
    }

    @PutMapping(value = "/{id}")
    public ResourceDTO update(@PathVariable(value = "id") long id,
                              @RequestBody ResourceDTO resource) {
        resource.setId(id);
        return resourceService.saveOrUpdate(resource);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable(value = "id") long id) {
        resourceService.delete(id);
    }

    @PostMapping(value = "/{id}/permissions") // Nested Resources
    public void savePermission(@PathVariable(value = "id") long resourceId,
                               @RequestBody PermissionForm permission) {
        resourceService.savePermission(resourceId, permission);
    }

    @DeleteMapping(value = "/{id}/permissions/{permission-id}") // Nested Resources
    public void deletePermission(@PathVariable(value = "id") long resourceId,
                                 @PathVariable(value = "permission-id") long permissionId) {
        resourceService.deletePermission(resourceId, permissionId);
    }

}
