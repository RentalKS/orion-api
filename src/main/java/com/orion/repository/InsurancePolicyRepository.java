package com.orion.repository;

import com.orion.dto.insurancePolicy.InsurancePolicyDto;
import com.orion.entity.InsurancePolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

public interface InsurancePolicyRepository extends JpaRepository<InsurancePolicy, Long> {

    @Query("SELECT i FROM InsurancePolicy i WHERE i.id = :id AND i.tenant.id = :tenantId")
    Optional<InsurancePolicy> findById(@Param("id") Long id, @Param("tenantId") Long tenantId);

    @Query("SELECT new com.orion.dto.insurancePolicy.InsurancePolicyDto(i.id, i.policyNumber, i.providerName, i.startDate, i.endDate, i.coverageDetails, i.vehicle.id) " +
            "FROM InsurancePolicy i WHERE i.id = :id AND i.tenant.id = :tenantId")
    Optional<InsurancePolicyDto> findDtoById(@Param("id") Long id, @Param("tenantId") Long tenantId);

    @Query("SELECT new com.orion.dto.insurancePolicy.InsurancePolicyDto(i.id, i.policyNumber, i.providerName, i.startDate, i.endDate, i.coverageDetails, i.vehicle.id) " +
            "FROM InsurancePolicy i WHERE i.tenant.id = :tenantId AND i.createdBy = :email")
    List<InsurancePolicyDto> findAllByTenant(@Param("tenantId") Long tenantId, @Param("email") String email);
}