package com.example.entity;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "role")
public class RoleEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    String title; // eg: Administrator
    @Column(updatable = false)

    @EqualsAndHashCode.Include
    String code; // eg: ROLE_ADMIN

    String description; // eg: Has full access to entire resources

    @Column(name = "is_active")
    boolean isActive;

    @ManyToMany(mappedBy = "roles")
    @OrderBy("id  ASC")
    Set<AccountEntity> accounts;

    @ManyToMany(fetch = FetchType.LAZY, targetEntity = PermissionEntity.class,
            cascade = { CascadeType.DETACH, CascadeType.MERGE,
                    CascadeType.REFRESH, CascadeType.PERSIST })
    @JoinTable(
        name = "role_permission",
        joinColumns = @JoinColumn(name = "role_id", nullable = false), foreignKey = @ForeignKey(name = "fk_role_permission"),
        inverseJoinColumns = @JoinColumn(name = "permission_id", nullable = false, foreignKey = @ForeignKey(name = "fk_permission_role"))
    )
    @OrderBy("id, title ASC")
    Set<PermissionEntity> permissions;

    public boolean addPermission(PermissionEntity permission) {
        if (Objects.isNull(this.permissions)) this.permissions = new HashSet<>();
        return this.permissions.add(permission);
                // doesn't need this: && permission.addRole(this);
    }
}
