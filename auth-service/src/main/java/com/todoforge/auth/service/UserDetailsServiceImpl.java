package com.todoforge.auth.service;

import com.todoforge.auth.client.AccountService;
import com.todoforge.auth.dto.AccountPermissionDTO;
import com.todoforge.auth.dto.TodoUser;
import com.todoforge.core.exception.BaseResponseException;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.todoforge.auth.dto.AccountPermissionDTO.*;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    AccountService accountService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AccountPermissionDTO accountPermission;
        try {
            accountPermission = accountService.getByUsername(username);
            if (Objects.isNull(accountPermission)) {
                throw new UsernameNotFoundException(String.format("%s not found", username));
            }
        } catch (BaseResponseException | HystrixRuntimeException baseResponseException) {
            throw new UsernameNotFoundException(String.format("%s not found", username));
        }
        AccountRoleDTO accountRole = accountPermission.getAccount();
        List<GrantedAuthority> authorities = this.buildGrantedAuthority(accountRole.getRoles(), accountPermission.getPermissions());
        return new TodoUser(username, accountRole.getPassword(), authorities, accountPermission);
    }

    private List<GrantedAuthority> buildGrantedAuthority(List<String> roles, List<ResourcePermission> permissions) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList(roles.size() + permissions.size());
        // role
        for (String role : roles) {
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        }
        // permission
        for (ResourcePermission rp : permissions) {
            grantedAuthorities.add(new SimpleGrantedAuthority(String.format("%s:%s", rp.getResourceCode(), rp.getScope())));
        }
        return grantedAuthorities;
    }
}
