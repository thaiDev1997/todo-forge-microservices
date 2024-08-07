package com.example.service.impl;

import com.example.constant.AccountStatus;
import com.example.dto.AccountDTO;
import com.example.entity.AccountEntity;
import com.example.entity.ProfileEntity;
import com.example.entity.RoleEntity;
import com.example.exception.BaseResponseException;
import com.example.repository.AccountRepository;
import com.example.repository.ProfileRepository;
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
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    @PersistenceContext
    EntityManager entityManager;
    ModelMapper modelMapper;

    AccountRepository accountRepository;
    RoleRepository roleRepository;
    ProfileRepository profileRepository;

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
        try {
            return entityManager.createQuery(cq).getSingleResult();
        } catch (NoResultException noResultException) {
            return null;
        }
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
            AccountDTO.ProfileDTO profile = account.getProfile();
            if (Objects.isNull(profile) && Objects.nonNull(accountEntity.getProfile())) {
                // avoid assign "null" to AccountEntity.profile
                ProfileEntity profileEntity = accountEntity.getProfile();
                profile = modelMapper.map(profileEntity, AccountDTO.ProfileDTO.class);
                account.setProfile(profile);
            }
            modelMapper.map(account, accountEntity);
        }
        // TODO: BcryptPassword -> accountEntity.setPassword(account.getPassword());
        accountEntity = accountRepository.saveAndFlush(accountEntity);
        account.setId(accountEntity.getId());
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

    @Transactional
    @Override
    public void deleteAllRoles(long id) {
        AccountEntity accountEntity = accountRepository.findById(id)
                .orElseThrow(() -> new BaseResponseException(HttpStatus.NOT_FOUND));
        Set<RoleEntity> roles = accountEntity.getRoles();
        // remove all roles out of account
        roles.clear();
        accountRepository.save(accountEntity);
    }

    @Transactional
    @Override
    public void deleteProfile(long id, long profileId) {
        AccountEntity accountEntity = accountRepository.findById(id)
                .orElseThrow(() -> new BaseResponseException(HttpStatus.NOT_FOUND));
        ProfileEntity profile = accountEntity.getProfile();
        if (profileId != profile.getId()) {
            throw new BaseResponseException(HttpStatus.BAD_REQUEST);
        }
        profileRepository.delete(profile);
    }
}
