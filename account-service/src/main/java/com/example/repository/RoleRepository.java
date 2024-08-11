package com.example.repository;

import com.example.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    @Query(value = "SELECT r.id, r.code " +
            "FROM account_role acc_role " +
            "INNER JOIN role r ON acc_role.role_id = r.id AND r.is_active = TRUE " +
            "WHERE acc_role.account_id = :accountId ", nativeQuery = true)
    List<Object[]> roles(@Param(value = "accountId") long accountId);
}
