package com.orion.repository;

import java.util.Optional;

import com.orion.dto.UserData;
import com.orion.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Query("SELECT new com.orion.dto.UserData(u.id, u.firstname,u.lastname, u.email,u.createdAt) FROM User u WHERE u.id = :id and u.deletedAt is null and u.deactivatedAt is null")
    Optional<UserData> findUserDataById(Integer id);

    @Query("SELECT new com.orion.dto.UserData(u.id, u.firstname,u.lastname, u.email,u.createdAt) FROM User u WHERE u.deletedAt is null and u.deactivatedAt is null")
    Optional<User> findUserById(Integer userId);

    @Query("SELECT ut FROM User ut WHERE ut.id=?1 AND ut.tenant.id=?2 AND ut.deletedAt IS null ")
    Optional<User> findByUserIdAndTenantIdAndDeletedAtIsNull(Long userId, Long tenantId);
}