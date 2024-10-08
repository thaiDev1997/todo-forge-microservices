package com.todoforge.account.dto;

import java.time.LocalDateTime;

public class RoleDTO extends ResourceDTO {
    private static final long serialVersionUID = 1L;

    public RoleDTO(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, String title, String code, String description, boolean isActive) {
        super(id, createdAt, updatedAt, title, code, description, isActive);
    }

    public RoleDTO(String code, String description, String title, boolean isActive) {
        super(title, code, description, isActive);
    }

}
