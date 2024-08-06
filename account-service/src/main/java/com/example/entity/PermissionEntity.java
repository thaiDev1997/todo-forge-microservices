package com.example.entity;

import com.example.constant.ScopePermission;
import com.example.converter.ScopeConverter;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "permission")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class PermissionEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    String title;
    String description;

    @Column(name = "is_active")
    boolean isActive;

    @EqualsAndHashCode.Include
    @Convert(converter = ScopeConverter.class) // name property rather than enum name
    ScopePermission scope;

    @ManyToMany(fetch = FetchType.LAZY, targetEntity = RoleEntity.class,
        cascade = { CascadeType.DETACH, CascadeType.MERGE,
                CascadeType.REFRESH, CascadeType.PERSIST })
    @JoinTable(
            name = "role_permission",
            inverseJoinColumns = @JoinColumn(name = "role_id", nullable = false), foreignKey = @ForeignKey(name = "fk_role_permission"),
            joinColumns = @JoinColumn(name = "permission_id", nullable = false, foreignKey = @ForeignKey(name = "fk_permission_role"))
    )
    @OrderBy("id, title ASC")
    Set<RoleEntity> roles;

    @ManyToOne
    @JoinColumn(name = "resource_id", foreignKey = @ForeignKey(name = "fk_permission_resource"), nullable = false)
    ResourceEntity resource;

    public boolean addRole(RoleEntity role) {
        if (Objects.isNull(this.roles)) this.roles = new HashSet<>();
        return this.roles.add(role);
    }
}
