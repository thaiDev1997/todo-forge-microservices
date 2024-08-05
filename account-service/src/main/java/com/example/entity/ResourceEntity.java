package com.example.entity;

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
@Table(name = "resource")
public class ResourceEntity extends BaseEntity {

    String title;
    String code;
    String description;

    @Column(name = "is_active")
    boolean isActive;

    @OneToMany(mappedBy = "resource", cascade = CascadeType.ALL)
    Set<PermissionEntity> permission;

}
