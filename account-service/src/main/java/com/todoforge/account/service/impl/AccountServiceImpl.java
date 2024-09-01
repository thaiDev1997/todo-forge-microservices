package com.todoforge.account.service.impl;

import com.todoforge.account.constant.AccountStatus;
import com.todoforge.account.dto.AccountDTO;
import com.todoforge.account.dto.AccountPermissionDTO;
import com.todoforge.account.dto.LastLoginDTO;
import com.todoforge.account.entity.AccountEntity;
import com.todoforge.account.entity.ProfileEntity;
import com.todoforge.account.entity.RoleEntity;
import com.todoforge.core.exception.BaseResponseException;
import com.todoforge.account.repository.AccountRepository;
import com.todoforge.account.repository.ProfileRepository;
import com.todoforge.account.repository.RoleRepository;
import com.todoforge.account.service.AccountService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.todoforge.account.dto.AccountPermissionDTO.AccountRoleDTO;
import static com.todoforge.account.dto.AccountPermissionDTO.ResourcePermission;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    @PersistenceContext
    EntityManager entityManager;
    ModelMapper modelMapper;
    PasswordEncoder passwordEncoder;

    AccountRepository accountRepository;
    RoleRepository roleRepository;
    ProfileRepository profileRepository;

    // readOnly = true
    // -> hope to omit Dirty Checking & tracking, prevent any write operations
    // -> also support Master-Slave
    @Transactional(readOnly = true)
    @Override
    public List<AccountDTO> getAll() {
        log.info("Account - getAll");
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
        String rawPassword = account.getPassword();
        if (id == 0) {
            accountEntity = accountRepository.getActive(username);
            if (Objects.nonNull(accountEntity)) {
                throw new BaseResponseException(HttpStatus.CONFLICT);
            }
            accountEntity = modelMapper.map(account, AccountEntity.class);
            accountEntity.setPassword(passwordEncoder.encode(rawPassword));
            accountEntity.setStatus(AccountStatus.ACTIVE);
        } else {
            accountEntity = accountRepository.getActive(id, username);
            if (Objects.isNull(accountEntity)) {
                throw new BaseResponseException(HttpStatus.NOT_FOUND);
            }
            String encodedPassword = accountEntity.getPassword();
            if (!passwordEncoder.matches(rawPassword, encodedPassword))
                encodedPassword = passwordEncoder.encode(rawPassword);

            AccountDTO.ProfileDTO profile = account.getProfile();
            if (Objects.isNull(profile) && Objects.nonNull(accountEntity.getProfile())) {
                // avoid assign "null" to AccountEntity.profile
                ProfileEntity profileEntity = accountEntity.getProfile();
                profile = modelMapper.map(profileEntity, AccountDTO.ProfileDTO.class);
                account.setProfile(profile);
            }
            modelMapper.map(account, accountEntity);
            accountEntity.setPassword(encodedPassword);
        }
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

    @Transactional(readOnly = true)
    @Override
    public AccountPermissionDTO getByUsername(String username) {
        // map by Constructor
        String jpql = "SELECT new " + AccountRoleDTO.class.getName() + "(account.id, account.username, account.password, " +
                "account.status, account.lastLogin, profile.firstName, profile.lastName, profile.emailAddress) " +
                "FROM " + AccountEntity.class.getName() + " account " +
                "INNER JOIN " + ProfileEntity.class.getName() + " profile ON account.id = profile.accountId " +
                "WHERE account.username = :username AND account.status = :status";
        TypedQuery<AccountRoleDTO> query = entityManager.createQuery(jpql, AccountRoleDTO.class);
        query.setParameter("username", username);
        query.setParameter("status", AccountStatus.ACTIVE);
        // account
        AccountRoleDTO account = query.getSingleResult();
        if (Objects.isNull(account)) throw new BaseResponseException(HttpStatus.NOT_FOUND);

        long accountId = account.getId();
        List<Object[]> roleIdCodes = roleRepository.roles(accountId);
        if (CollectionUtils.isEmpty(roleIdCodes)) throw new BaseResponseException(HttpStatus.NOT_FOUND);
        List<String> roleCodes = roleIdCodes
                .parallelStream()
                .map(objects -> (String) objects[1])
                .collect(Collectors.toList());
        account.setRoles(roleCodes);

        List<Long> roleIds = roleIdCodes
                .parallelStream()
                .map(objects -> ((BigInteger) objects[0]).longValue())
                .collect(Collectors.toList());
        List<ResourcePermission> resourcePermissions = this.getResourcePermissions(accountId, roleIds);
        return new AccountPermissionDTO(account, resourcePermissions);
    }

    @Transactional
    @Override
    public void updateLastLogin(LastLoginDTO lastLogin) {
        accountRepository.updateLastLogin(lastLogin.getUsername(), lastLogin.getLocalDateTime());
    }

    private List<ResourcePermission> getResourcePermissions(long accountId, List<Long> roleIds) {
        String nativeSQL = "SELECT  r.code, p.scope " +
                "FROM role role " +
                "INNER JOIN account_role acc_role ON role.id = acc_role.role_id AND acc_role.account_id = :accountId " +
                "INNER JOIN role_permission role_per ON role.id = role_per.role_id " +
                "INNER JOIN permission p ON role_per.permission_id = p.id AND p.is_active = TRUE " +
                "INNER JOIN resource r ON p.resource_id = r.id AND r.is_active = TRUE " +
                "WHERE role.is_active = true and role.id IN (:roleIds)";
        Query nativeQuery = entityManager.createNativeQuery(nativeSQL, ResourcePermission.GET_RESOURCE_PERMISSION_MAPPING);
        nativeQuery.setParameter("accountId", accountId);
        nativeQuery.setParameter("roleIds", roleIds);
        return nativeQuery.getResultList();
    }
}
