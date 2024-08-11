package com.example.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
@Setter
public class TodoUser extends User {

    String firstName;
    String lastName;
    String emailAddress;

    public TodoUser(String username, String password, Collection<? extends GrantedAuthority> authorities, AccountDTO account) {
        super(username, password, authorities);
        AccountDTO.ProfileDTO profile = account.getProfile();
        this.firstName = profile.getFirstName();
        this.lastName = profile.getLastName();
        this.emailAddress = profile.getEmailAddress();
    }
}
