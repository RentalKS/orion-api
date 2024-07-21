package com.orion.dto.insurancePolicy;

import lombok.Value;

import java.time.LocalDateTime;

/**
 * DTO for {@link com.orion.entity.InsurancePolicy}
 */
@Value
public class InsurancePolicyDto {
    Long id;
    String policyNumber;
    String providerName;
    LocalDateTime startDate;
    LocalDateTime endDate;
    String coverageDetails;
    Long vehicleId;
}