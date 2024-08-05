package com.example.service.impl;

import com.example.constant.AccountStatus;
import com.example.dto.AccountDTO;
import com.example.entity.AccountEntity;
import com.example.entity.AccountEntity;
import com.example.exception.BaseResponseException;
import com.example.repository.AccountRepository;
import com.example.service.AccountService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    @PersistenceContext
    EntityManager entityManager;
    AccountRepository accountRepository;
    ModelMapper modelMapper;

    @Override
    public List<AccountDTO> getAll() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<AccountDTO> cq = cb.createQuery(AccountDTO.class);
        Root<AccountEntity> accountRoot = cq.from(AccountEntity.class);
        // Set the query's selection to the constructor expression
        cq.select(cb.construct(
                AccountDTO.class,
                accountRoot.get("id"),
                accountRoot.get("createdAt"),
                accountRoot.get("updatedAt"),
                accountRoot.get("username"),
                accountRoot.get("password"),
                accountRoot.get("status"),
                accountRoot.get("lastLogin")
        ));
        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    public AccountDTO getDetail(long id) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<AccountDTO> cq = cb.createQuery(AccountDTO.class);
        Root<AccountEntity> accountRoot = cq.from(AccountEntity.class);
        // Set the query's selection to the constructor expression
        cq.select(cb.construct(
                AccountDTO.class,
                accountRoot.get("id"),
                accountRoot.get("createdAt"),
                accountRoot.get("updatedAt"),
                accountRoot.get("username"),
                accountRoot.get("password"),
                accountRoot.get("status"),
                accountRoot.get("lastLogin")
        ));
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(accountRoot.get("id"), id));
        cq.where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(cq).getSingleResult();
    }

    @Transactional
    @Override
    public AccountDTO saveOrUpdate(AccountDTO account) {
        AccountEntity accountEntity;
        long id = account.getId();
        String username = account.getUsername();
        if (id == 0) {
            accountEntity = accountRepository.getActive(username);
            if (Objects.nonNull(accountEntity)) {
                throw new BaseResponseException(HttpStatus.CONFLICT);
            }
            accountEntity = modelMapper.map(account, AccountEntity.class);
            accountEntity.setStatus(AccountStatus.ACTIVE);
            accountEntity.setCreatedAt(LocalDateTime.now());
        } else {
            accountEntity = accountRepository.getActive(id, username);
            if (Objects.isNull(accountEntity)) {
                throw new BaseResponseException(HttpStatus.NOT_FOUND);
            }

            // TODO: BcryptPassword -> accountEntity.setPassword(account.getPassword());
            accountEntity.setStatus(account.getStatus());
            accountEntity.setUpdatedAt(LocalDateTime.now());
        }
        accountRepository.saveAndFlush(accountEntity);

        modelMapper.map(accountEntity, account);
        return account;
    }

    @Transactional
    @Override
    public void delete(long id) {
        if (id > 0) {
            accountRepository.deleteById(id);
        }
    }
}
