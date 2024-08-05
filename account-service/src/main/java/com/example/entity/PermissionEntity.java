package com.example.entity;

import com.example.constant.ScopePermission;
import com.example.converter.ScopeConverter;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "permission")
public class PermissionEntity extends BaseEntity {

    String title;
    String description;

    @Column(name = "is_active")
    boolean isActive;

    @Convert(converter = ScopeConverter.class) // name property rather than enum name
    private ScopePermission role;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "permissions")
    @OrderBy("id, title ASC")
    Set<RoleEntity> roles;

    @ManyToOne
    @JoinColumn(name = "resource_id", foreignKey = @ForeignKey(name = "fk_permission_resource"), nullable = false)
    ResourceEntity resource;
}
