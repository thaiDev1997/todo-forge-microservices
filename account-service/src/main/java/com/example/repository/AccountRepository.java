package com.example.repository;

import com.example.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

    @Query(value = "SELECT account " +
            "FROM AccountEntity account " +
            "WHERE username = :username AND isActive = TRUE")
    AccountEntity getActive(@Param(value = "username") String username);

    @Query(value = "SELECT account " +
            "FROM AccountEntity account " +
            "WHERE id = :id AND username = :username AND isActive = TRUE")
    AccountEntity getActive(@Param(value = "id") long id,
                            @Param(value = "username") String username);
}
