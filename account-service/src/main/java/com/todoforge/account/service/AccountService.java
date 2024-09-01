package com.todoforge.account.service;

import com.todoforge.account.dto.AccountDTO;
import com.todoforge.account.dto.AccountPermissionDTO;
import com.todoforge.account.dto.LastLoginDTO;

import java.util.List;

public interface AccountService {
    List<AccountDTO> getAll();
    AccountDTO getDetail(long id);
    AccountDTO saveOrUpdate(AccountDTO todo);
    void delete(long id);
    void saveRole(long id, long roleId);
    void deleteRole(long id, long roleId);
    void deleteAllRoles(long id);
    void deleteProfile(long id, long profileId);
    AccountPermissionDTO getByUsername(String username);
    void updateLastLogin(LastLoginDTO lastLogin);
}
