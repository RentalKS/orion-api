package com.orion.repository;

import java.util.Optional;

import com.orion.dto.user.UserData;
import com.orion.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Query("SELECT new com.orion.dto.user.UserData(u.id, u.firstname,u.lastname, u.email,u.createdAt,u.role.name) FROM User u WHERE u.email = :email and u.deletedAt is null and u.deactivatedAt is null and u.tenant.id = :tenantId")
    Optional<UserData> findUserDataById(@Param("email") String email, @Param("tenantId") Long tenantId);

    @Query("SELECT new com.orion.dto.user.UserData(u.id, u.firstname,u.lastname, u.email,u.createdAt,u.role.name) FROM User u WHERE u.id = :userId and u.deletedAt is null and u.deactivatedAt is null")
    Optional<UserData> findUserById(@Param("userId") Long userId);

    @Query("SELECT u FROM User u WHERE u.id = :userId and u.deletedAt is null and u.deactivatedAt is null")
    Optional<User> findUserIdDeleteAtNull(@Param("userId") Long userId);

    @Query("SELECT ut FROM User ut WHERE ut.id=?1 AND ut.tenant.id=?2 AND ut.deletedAt IS null ")
    Optional<User> findByUserIdAndTenantIdAndDeletedAtIsNull(Long userId, Long tenantId);
}