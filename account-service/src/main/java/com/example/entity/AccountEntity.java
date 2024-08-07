package com.example.entity;

import com.example.constant.AccountStatus;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
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

    // account is owner side of relationship
    @EqualsAndHashCode.Exclude
    @ManyToMany(fetch = FetchType.LAZY, targetEntity = RoleEntity.class,
            cascade = { CascadeType.DETACH, CascadeType.MERGE,
                    CascadeType.REFRESH, CascadeType.PERSIST })
    @JoinTable(
        name = "account_role",
        joinColumns = @JoinColumn(name = "account_id", nullable = false, foreignKey = @ForeignKey(name = "fk_account_role")),
        inverseJoinColumns = @JoinColumn(name = "role_id", nullable = false, foreignKey = @ForeignKey(name = "fk_role_account"))
    )
    @OrderBy("id, title ASC")
    Set<RoleEntity> roles;

    @EqualsAndHashCode.Exclude
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "account", cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
    ProfileEntity profile;

    public boolean addRole(RoleEntity role) {
        if (Objects.isNull(this.roles)) this.roles = new HashSet<>();
        return this.roles.add(role);
    }
}
