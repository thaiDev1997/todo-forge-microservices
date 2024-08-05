package com.example.service;

import com.example.dto.RoleDTO;

import java.util.List;

public interface RoleService {
    List<RoleDTO> getAll();
    RoleDTO getDetail(long id);
    RoleDTO saveOrUpdate(RoleDTO todo);
    void delete(long id);
}
