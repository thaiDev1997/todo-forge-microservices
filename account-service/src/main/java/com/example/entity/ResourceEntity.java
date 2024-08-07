package com.example.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "resource")
@NoArgsConstructor
public class ResourceEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    String title;
    @Column(updatable = false, nullable = false)
    String code;
    String description;

    @Column(name = "is_active")
    boolean isActive;

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "resource", cascade = CascadeType.ALL)
    Set<PermissionEntity> permissions;

    public boolean addPermission(PermissionEntity permissionEntity) {
        if (Objects.isNull(permissions)) this.permissions = new HashSet<>();
        permissionEntity.setResource(this);
        return permissions.add(permissionEntity);
    }

}
