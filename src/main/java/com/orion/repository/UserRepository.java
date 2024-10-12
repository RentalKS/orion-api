package com.orion.repository;

import java.util.List;
import java.util.Optional;

import com.orion.dto.user.UserData;
import com.orion.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.email = :email and u.deletedAt is null and u.deactivatedAt is null")
    Optional<User> findByEmail(@Param("email") String email);

    @Query("SELECT new com.orion.dto.user.UserData(u.id, u.firstname,u.lastname, u.email,u.createdAt,u.role.name) FROM User u WHERE u.email = :email and u.deletedAt is null and u.deactivatedAt is null ")
    Optional<UserData> findUserDataById(@Param("email") String email);

    @Query("SELECT new com.orion.dto.user.UserData(u.id, u.firstname,u.lastname, u.email,u.createdAt,u.role.name) FROM User u WHERE u.id = :userId and u.deletedAt is null and u.deactivatedAt is null")
    Optional<UserData> findUserById(@Param("userId") Long userId);

    @Query("SELECT u FROM User u WHERE u.id = :userId and u.deletedAt is null and u.deactivatedAt is null and u.tenant.id = :tenantId")
    Optional<User> findUserIdDeleteAtNull(@Param("userId") Long userId, @Param("tenantId") Long tenantId);

    @Query("SELECT ut FROM User ut WHERE ut.id=?1 AND ut.tenant.id=?2 AND ut.deletedAt IS null ")
    Optional<User> findByUserIdAndTenantIdAndDeletedAtIsNull(Long userId, Long tenantId);

    @Query("SELECT u.email from User u where u.tenant.id = :id and u.deactivatedAt is null and u.deletedAt is null ")
    List<String> findEmailsOfUsersByTenant(Long id);

    @Query("SELECT u.email from User u where u.tenant.id = :tenantId and u.deactivatedAt is null and u.deletedAt is null ")
    List<String> findAgencyIdsByTenant(@Param("tenantId") Long tenantId);

    @Query("SELECT u.id from User u where u.tenant.id = :tenantId and u.deactivatedAt is null and u.deletedAt is null and u.role.name = 'CUSTOMER'")
    List<Long> findUsersByTenant(@Param("tenantId") Long tenantId);

    @Query("SELECT new com.orion.dto.user.UserData(u.id, u.firstname,u.lastname, u.email,u.createdAt,u.role.name) " +
            "FROM User u WHERE u.tenant.id = :tenantId" +
            " and u.deactivatedAt is null and u.deletedAt is null ")
    List<UserData> findUsersIdsByTenant(@Param("tenantId") Long tenantId);

}