package com.example.service;

import com.example.dto.AccountDTO;

import java.util.List;

public interface AccountService {
    List<AccountDTO> getAll();
    AccountDTO getDetail(long id);
    AccountDTO saveOrUpdate(AccountDTO todo);
    void delete(long id);
}
