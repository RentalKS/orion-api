package com.orion.repository;

import com.orion.dto.rates.RatesDto;
import com.orion.entity.RateDates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface RateDatesRepository extends JpaRepository<RateDates, Long> {
    @Query("SELECT r FROM RateDates r WHERE r.id = :rateId and r.deletedAt is null and r.tenant.id = :tenantId")
    Optional<RateDates> findRateById(@Param("rateId") Long rateId, @Param("tenantId") Long tenantId);

    @Query("SELECT new com.orion.dto.rates.RatesDto(r.id,r.name,r.dailyRate,r.weeklyRate,r.monthlyRate) FROM RateDates r WHERE r.id = :id and r.deletedAt is null and r.createdBy = :currentEmail")
    Optional<RatesDto> findByRateDates(@Param("id") Long id,@Param("currentEmail") String currentEmail);

    @Query("SELECT new com.orion.dto.rates.RatesDto(r.id,r.name,r.dailyRate,r.weeklyRate,r.monthlyRate)  FROM RateDates r WHERE r.deletedAt is null and r.createdBy = :currentEmail")
    List<RatesDto> findAllRateDates(@Param("currentEmail") String currentEmail);
}