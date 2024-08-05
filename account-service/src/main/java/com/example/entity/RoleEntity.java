package com.example.entity;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.Set;

@EqualsAndHashCode
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "role")
public class RoleEntity extends BaseEntity {

    String title; // eg: Admin
    String code; // eg: ROLE_ADMIN
    String description; // eg: Has full access to entire resources

    @Column(name = "is_active")
    boolean isActive;

    @ManyToMany(mappedBy = "roles")
    @OrderBy("id  ASC")
    Set<AccountEntity> accounts;

    @ManyToMany
    @JoinTable(
        name = "role_permission",
        joinColumns = @JoinColumn(name = "role_id", nullable = false), foreignKey = @ForeignKey(name = "fk_role_permission"),
        inverseJoinColumns = @JoinColumn(name = "permission_id", nullable = false, foreignKey = @ForeignKey(name = "fk_permission_role"))
    )
    @OrderBy("id, title ASC")
    Set<PermissionEntity> permissions;
}
