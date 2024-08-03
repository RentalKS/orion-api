package com.orion.repository;

import com.orion.entity.RateDates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RateDatesRepository extends JpaRepository<RateDates, Long> {
    @Query("SELECT r FROM RateDates r WHERE r.id = :rateId and r.deletedAt is null and r.tenant.id = :tenantId")
    Optional<RateDates> findRateById(@Param("rateId") Long rateId, @Param("tenantId") Long tenantId);
}