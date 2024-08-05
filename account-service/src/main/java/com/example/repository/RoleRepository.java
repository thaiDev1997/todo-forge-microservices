package com.example.repository;

import com.example.entity.RoleEntity;
import com.example.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    @Query(value = "SELECT role " +
            "FROM RoleEntity role " +
            "WHERE code = :code AND isActive = TRUE")
    RoleEntity getActive(@Param(value = "code") String code);

    @Query(value = "SELECT role " +
            "FROM RoleEntity role " +
            "WHERE id = :id AND code = :code AND isActive = TRUE")
    RoleEntity getActive(@Param(value = "id") long id,
                         @Param(value = "code") String code);
}
