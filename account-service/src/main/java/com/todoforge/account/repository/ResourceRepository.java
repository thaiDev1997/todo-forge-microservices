package com.todoforge.account.repository;

import com.todoforge.account.entity.ResourceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceRepository extends JpaRepository<ResourceEntity, Long> {

    @Query(value = "SELECT resource " +
            "FROM ResourceEntity resource " +
            "WHERE code = :code AND isActive = TRUE")
    ResourceEntity getActive(@Param(value = "code") String code);

    @Query(value = "SELECT resource " +
            "FROM ResourceEntity resource " +
            "WHERE id = :id AND code = :code AND isActive = TRUE")
    ResourceEntity getActive(@Param(value = "id") long id,
                             @Param(value = "code") String code);

}
