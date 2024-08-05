package com.example.entity;

import com.example.constant.AccountStatus;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "account")
public class AccountEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Column(updatable = false)
    String username;
    String password;

    @Enumerated(EnumType.STRING)
    AccountStatus status;

    @Column(name = "last_login")
    LocalDateTime lastLogin;

    @EqualsAndHashCode.Exclude
    @ManyToMany
    @JoinTable(
        name = "account_role",
        joinColumns = @JoinColumn(name = "account_id", nullable = false, foreignKey = @ForeignKey(name = "fk_account_role")),
        inverseJoinColumns = @JoinColumn(name = "role_id", nullable = false, foreignKey = @ForeignKey(name = "fk_role_account"))
    )
    @OrderBy("id, title ASC")
    Set<RoleEntity> roles;

    @EqualsAndHashCode.Exclude
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "account", cascade = CascadeType.ALL)
    ProfileEntity profile;
}
