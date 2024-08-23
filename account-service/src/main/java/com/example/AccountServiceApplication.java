package com.example;

import com.example.constant.AccountStatus;
import com.example.constant.ScopePermission;
import com.example.dto.AccountDTO;
import com.example.dto.ResourceDTO;
import com.example.dto.RoleDTO;
import com.example.entity.AccountEntity;
import com.example.entity.ResourceEntity;
import com.example.entity.RoleEntity;
import com.example.exception.BaseResponseException;
import com.example.form.PermissionForm;
import com.example.repository.AccountRepository;
import com.example.repository.ResourceRepository;
import com.example.repository.RoleRepository;
import com.example.service.AccountService;
import com.example.service.ResourceService;
import com.example.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@EnableCircuitBreaker
@EnableJpaAuditing
@EnableDiscoveryClient
@SpringBootApplication
public class AccountServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AccountServiceApplication.class, args);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public CommandLineRunner initData(PasswordEncoder passwordEncoder,
                                      AccountRepository accountRepository, AccountService accountService,
                                      RoleRepository roleRepository, RoleService roleService,
                                      ResourceRepository resourceRepository, ResourceService resourceService) {
        return (args) -> {
            // account
            final String adminUsername = "admin", rawPassword = "test123";
            ;

            AccountEntity accountEntity = accountRepository.getActive(adminUsername);
            if (Objects.isNull(accountEntity)) {
                log.info("Initialize account");
                // profile
                AccountDTO.ProfileDTO profile = AccountDTO.ProfileDTO.builder()
                        .firstName("Administrator")
                        .lastName("Administrator")
                        .emailAddress("administrator@todo.com").build();

                AccountDTO account = AccountDTO.builder()
                        .username(adminUsername)
                        .password(rawPassword)
                        .status(AccountStatus.ACTIVE)
                        .profile(profile)
                        .build();
                accountService.saveOrUpdate(account);
                accountEntity = accountRepository.getActive(adminUsername);
            }

            // role
            final String adminCode = "ADMIN";
            RoleEntity roleEntity = roleRepository.getActive(adminCode);
            if (Objects.isNull(roleEntity)) {
                log.info("Initialize role");
                RoleDTO role = new RoleDTO(adminCode, "Administrator", "Administrator", true);
                roleService.saveOrUpdate(role);
                roleEntity = roleRepository.getActive(adminCode);
            }

            // account_role
            try {
                accountService.saveRole(accountEntity.getId(), roleEntity.getId());
                log.info("Assign role to account");
            } catch (BaseResponseException baseResponseException) {
                if (!HttpStatus.CONFLICT.equals(baseResponseException.getStatus())) {
                    log.error(baseResponseException.getMessage(), baseResponseException);
                }
            }

            // resource
            String todoResource = "todo";
            ResourceEntity resourceEntity = resourceRepository.getActive(todoResource);
            if (Objects.isNull(resourceEntity)) {
                ResourceDTO resource = ResourceDTO.builder()
                        .code(todoResource)
                        .title("Todo Resource")
                        .description("Todo Resource").isActive(true).build();
                try {
                    resourceService.saveOrUpdate(resource);
                    log.info("Initialize Resource");
                } catch (BaseResponseException baseResponseException) {
                    if (!HttpStatus.CONFLICT.equals(baseResponseException.getStatus())) {
                        log.error(baseResponseException.getMessage(), baseResponseException);
                    }
                }
                resourceEntity = resourceRepository.getActive(todoResource);
            }

            // permission
            List<Long> permissionIds = Collections.EMPTY_LIST;
            try {
                PermissionForm readTodoPermission = PermissionForm.builder()
                        .title("Todo Resource - READ")
                        .description("todo:read")
                        .scope(ScopePermission.READ)
                        .build();
                PermissionForm writeTodoPermission = PermissionForm.builder()
                        .title("Todo Resource - WRITE")
                        .description("todo:write")
                        .scope(ScopePermission.WRITE)
                        .build();

                permissionIds = new ArrayList<>(2);
                permissionIds.add(resourceService.savePermission(resourceEntity.getId(), readTodoPermission));
                permissionIds.add(resourceService.savePermission(resourceEntity.getId(), writeTodoPermission));
                log.info("Assign permissions to resource");
            } catch (BaseResponseException baseResponseException) {
                if (!HttpStatus.CONFLICT.equals(baseResponseException.getStatus())) {
                    log.error(baseResponseException.getMessage(), baseResponseException);
                }
            }

            // role_permission
            try {
                for (Long permissionId : permissionIds) {
                    log.info("Assign permissions to role");
                    roleService.savePermission(roleEntity.getId(), permissionId);
                }
            } catch (BaseResponseException baseResponseException) {
            }
        };
    }
}