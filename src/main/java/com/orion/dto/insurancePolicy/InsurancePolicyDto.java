package com.orion.dto.insurancePolicy;

import com.orion.util.DateUtil;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Value;

import java.time.LocalDateTime;

/**
 * DTO for {@link com.orion.entity.InsurancePolicy}
 */
@Getter
@Setter
@NoArgsConstructor
public class InsurancePolicyDto {
    private Long id;
    @NotBlank
    private String policyNumber;
    @NotBlank
    private String providerName;
    private String coverageDetails;
    private Long vehicleId;

    public InsurancePolicyDto(Long id, String policyNumber, String providerName, String coverageDetails, Long vehicleId) {
        this.id = id;
        this.policyNumber = policyNumber;
        this.providerName = providerName;
        this.coverageDetails = coverageDetails;
        this.vehicleId = vehicleId;
    }
}