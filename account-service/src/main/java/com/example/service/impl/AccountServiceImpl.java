package com.example.service.impl;

import com.example.constant.AccountStatus;
import com.example.dto.AccountDTO;
import com.example.entity.AccountEntity;
import com.example.entity.RoleEntity;
import com.example.exception.BaseResponseException;
import com.example.repository.AccountRepository;
import com.example.repository.RoleRepository;
import com.example.service.AccountService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.collections4.CollectionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    @PersistenceContext
    EntityManager entityManager;
    ModelMapper modelMapper;

    AccountRepository accountRepository;
    RoleRepository roleRepository;

    // readOnly = true
    // -> hope to omit Dirty Checking & tracking, prevent any write operations
    // -> also support Master-Slave
    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
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
        } else {
            accountEntity = accountRepository.getActive(id, username);
            if (Objects.isNull(accountEntity)) {
                throw new BaseResponseException(HttpStatus.NOT_FOUND);
            }
            accountEntity.setStatus(account.getStatus());
        }
        // TODO: BcryptPassword -> accountEntity.setPassword(account.getPassword());
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

    @Transactional
    @Override
    public void saveRole(long id, long roleId) {
        AccountEntity accountEntity = accountRepository.findById(id)
                .orElseThrow(() -> new BaseResponseException(HttpStatus.NOT_FOUND));
        RoleEntity roleEntity = roleRepository.findById(roleId)
                .orElseThrow(() -> new BaseResponseException(HttpStatus.NOT_FOUND));
        boolean result = accountEntity.addRole(roleEntity);
        if (!result) {
            throw new BaseResponseException(HttpStatus.CONFLICT);
        }
        accountRepository.save(accountEntity);
    }

    @Transactional
    @Override
    public void deleteRole(long id, long roleId) {
        AccountEntity accountEntity = accountRepository.findById(id)
                .orElseThrow(() -> new BaseResponseException(HttpStatus.NOT_FOUND));
        Set<RoleEntity> roles = accountEntity.getRoles();
        if (CollectionUtils.isEmpty(roles)) return;
        RoleEntity roleEntity = roleRepository.findById(roleId)
                .orElseThrow(() -> new BaseResponseException(HttpStatus.NOT_FOUND));
        boolean result = roles.remove(roleEntity);
        if (!result) throw new BaseResponseException(HttpStatus.BAD_REQUEST);
        accountRepository.save(accountEntity);
    }
}
