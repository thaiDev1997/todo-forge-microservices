package com.example.repository;

import com.example.constant.AccountStatus;
import com.example.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

    @Deprecated // except ACTIVE can return multiple results -> NonUniqueResultException
    @Query(value = "SELECT account " +
            "FROM AccountEntity account " +
            "WHERE username = :username AND status = :status")
    AccountEntity getActive(@Param(value = "username") String username,
                            @Param(value = "status") AccountStatus status);

    default AccountEntity getActive(String username) {
        return this.getActive(username, AccountStatus.ACTIVE);
    }

    @Deprecated // except ACTIVE can return multiple results -> NonUniqueResultException
    @Query(value = "SELECT account " +
            "FROM AccountEntity account " +
            "WHERE id = :id AND username = :username AND status = :status")
    AccountEntity getActive(@Param(value = "id") long id,
                            @Param(value = "username") String username,
                            @Param(value = "status") AccountStatus status);

    default AccountEntity getActive(long id, String username) {
        return this.getActive(id, username, AccountStatus.ACTIVE);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Modifying
    @Query(value = "UPDATE AccountEntity account " +
            "SET account.lastLogin = :lastLogin " +
            "WHERE username = :username " +
            "AND (account.lastLogin IS NULL OR :lastLogin > account.lastLogin)")
    Integer updateLastLogin(@Param("username") String username,
                            @Param("lastLogin") LocalDateTime lastLogin);
}
