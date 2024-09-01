package com.todoforge.account.entity;

import com.todoforge.account.dto.AccountPermissionDTO;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@SqlResultSetMapping(
    name = AccountPermissionDTO.ResourcePermission.GET_RESOURCE_PERMISSION_MAPPING,
    classes =
        @ConstructorResult(
            targetClass = AccountPermissionDTO.ResourcePermission.class,
            columns = {
                @ColumnResult(name = "code", type = String.class),
                @ColumnResult(name = "scope", type = String.class)
            }
        )
)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "\"role\"")
public class RoleEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    String title; // eg: Administrator

    @Column(updatable = false, nullable = false)
    @EqualsAndHashCode.Include
    String code; // eg: ROLE_ADMIN

    String description; // eg: Has full access to entire resources

    @Column(name = "is_active")
    boolean isActive;

    @ManyToMany(fetch = FetchType.LAZY, targetEntity = AccountEntity.class,
            cascade = { CascadeType.DETACH, CascadeType.MERGE,
                    CascadeType.REFRESH, CascadeType.PERSIST })
    @JoinTable(
            name = "account_role",
            joinColumns = @JoinColumn(name = "role_id", nullable = false, foreignKey = @ForeignKey(name = "fk_role_account")),
            inverseJoinColumns = @JoinColumn(name = "account_id", nullable = false, foreignKey = @ForeignKey(name = "fk_account_role"))
    )
    // @OrderBy("id ASC") -> use TreeSet or List instead of
    Set<AccountEntity> accounts;

    // role is owner side of relationship
    @ManyToMany(fetch = FetchType.LAZY, targetEntity = PermissionEntity.class,
            cascade = { CascadeType.DETACH, CascadeType.MERGE,
                    CascadeType.REFRESH, CascadeType.PERSIST })
    @JoinTable(
        name = "role_permission",
        joinColumns = @JoinColumn(name = "role_id", nullable = false), foreignKey = @ForeignKey(name = "fk_role_permission"),
        inverseJoinColumns = @JoinColumn(name = "permission_id", nullable = false, foreignKey = @ForeignKey(name = "fk_permission_role"))
    )
    // @OrderBy("id, title ASC") -> use TreeSet or List instead of
    Set<PermissionEntity> permissions;

    public boolean addPermission(PermissionEntity permission) {
        if (Objects.isNull(this.permissions)) this.permissions = new HashSet<>();
        return this.permissions.add(permission);
        // doesn't need this: && permission.addRole(this);
    }
}
