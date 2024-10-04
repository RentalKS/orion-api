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
    private Long startDate;
    private Long endDate;
    private String coverageDetails;
    private Long vehicleId;

    public InsurancePolicyDto(Long id, String policyNumber, String providerName, LocalDateTime startDate, LocalDateTime endDate, String coverageDetails, Long vehicleId) {
        this.id = id;
        this.policyNumber = policyNumber;
        this.providerName = providerName;
        this.startDate = DateUtil.localDateTimeToMilliseconds(startDate);
        this.endDate = DateUtil.localDateTimeToMilliseconds(endDate);
        this.coverageDetails = coverageDetails;
        this.vehicleId = vehicleId;
    }
    public InsurancePolicyDto(Long id, String policyNumber, String providerName, String coverageDetails, Long vehicleId) {
        this.id = id;
        this.policyNumber = policyNumber;
        this.providerName = providerName;
        this.coverageDetails = coverageDetails;
        this.vehicleId = vehicleId;
    }
    public LocalDateTime getStartDate(){
        if(startDate == null){
            return null;
        }
        return DateUtil.convertToLocalDateTime(startDate);
    }
    public LocalDateTime getEndDate(){
        if(endDate == null){
            return null;
        }
        return DateUtil.convertToLocalDateTime(endDate);
    }
}