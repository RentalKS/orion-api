package com.orion.repository;

import com.orion.dto.tenant.TenantDataDto;
import com.orion.entity.Tenant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {
    @Query("select t from Tenant t where t.id = :tenantId and t.deletedAt is null")
    Optional<Tenant> findTenantById(@Param("tenantId") Long tenantId);

    @Query("SELECT new com.orion.dto.tenant.TenantDataDto(" +
            " t.id, t.name, t.domain, ut.firstname, ut.lastname, ut.email" +
            ") " +
            "FROM Tenant t " +
            "JOIN User ut ON ut.tenant.id=t.id " +
            "WHERE t.deletedAt IS NULL AND ut.role.name = 'TENANT'")
    Page<TenantDataDto> findAllTenants(Pageable pageable);

    @Query("SELECT new com.orion.dto.tenant.TenantDataDto(" +
            " t.id, t.name, t.domain, ut.firstname, ut.lastname, ut.email" +
            ") " +
            "FROM Tenant t " +
            "JOIN User ut ON ut.tenant.id=t.id " +
            "WHERE t.deletedAt IS NULL AND ut.role.name = 'TENANT' AND (t.name LIKE %:search%)")
    Page<TenantDataDto> findAllTenantsWithSearch(@Param("search") String search, Pageable pageable);

    Optional<Tenant> findByDomain(String domain);

    @Query("Select t.domain from Tenant t where t.id=?1")
    String findDomain(Long TenantId);

    @Query("SELECT t FROM Tenant t " +
            "JOIN User u ON u.tenant.id=t.id AND u.id=:userId " +
            "WHERE t.deletedAt IS NULL")
    List<Tenant> findByUserId(Long userId);


    @Query("SELECT t.id FROM Tenant t where t.domain = ?1 AND t.id<>?2 AND t.deletedAt IS NULL")
    Optional<Tenant> findByDomainAndDeletedAtAndId(String domain, Long tenantId);
}
