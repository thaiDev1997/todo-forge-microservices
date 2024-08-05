package com.example.entity;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "role")
public class RoleEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    String title; // eg: Admin
    @Column(updatable = false)
    String code; // eg: ROLE_ADMIN
    String description; // eg: Has full access to entire resources

    @Column(name = "is_active")
    boolean isActive;

    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "roles")
    @OrderBy("id  ASC")
    Set<AccountEntity> accounts;

    @EqualsAndHashCode.Exclude
    @ManyToMany
    @JoinTable(
        name = "role_permission",
        joinColumns = @JoinColumn(name = "role_id", nullable = false), foreignKey = @ForeignKey(name = "fk_role_permission"),
        inverseJoinColumns = @JoinColumn(name = "permission_id", nullable = false, foreignKey = @ForeignKey(name = "fk_permission_role"))
    )
    @OrderBy("id, title ASC")
    Set<PermissionEntity> permissions;
}
