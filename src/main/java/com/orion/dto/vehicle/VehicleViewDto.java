package com.orion.dto.vehicle;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.orion.dto.insurancePolicy.InsurancePolicyDto;
import com.orion.dto.maintenanceRecord.MaintenanceRecordDto;
import com.orion.dto.model.ModelDto;
import com.orion.dto.rates.RatesDto;
import com.orion.dto.section.SectionDto;
import com.orion.enums.vehicle.*;
import com.orion.util.DateUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VehicleViewDto extends VehicleDto {
    private Long createdAt;
    private Long id;
    private ModelDto model;
    private RatesDto rateDates;
    private InsurancePolicyDto insurancePolicy;
    private SectionDto section;
    private List<MaintenanceRecordDto> maintenanceRecord;
    private RentalStatus rentalStatus;
    private String createdByName;
    private Long createdById;
    private Long insuranceId;

    public VehicleViewDto(LocalDateTime createdAt, Long id, Long modelId, String modelName, Long brandId, String type,
                          Long seatingCapacity, String fuelEfficiency, String modelImageUrl, Long rateId, String name,
                          Double dailyRate, Double weeklyRate, Double monthlyRate, Long insuranceId, String policyNumber,
                          String providerName, String coverageDetails, Long vehicleId,
                          Long sectionId, LocalDateTime createdAtSection, String sectionName, String sectionDescription,
                          String sectionImageUrl, Long categoryId,String createdByName, Long createdById) {

        this.createdAt = DateUtil.localDateTimeToMilliseconds(createdAt);
        this.id = id;
        this.model = new ModelDto(modelId,modelName,brandId,type,seatingCapacity,fuelEfficiency,modelImageUrl);
        this.rateDates = new RatesDto(rateId,name,dailyRate,weeklyRate,monthlyRate);
        this.insurancePolicy = new InsurancePolicyDto(insuranceId,policyNumber,providerName,coverageDetails,vehicleId);
        this.section = new SectionDto(sectionId,createdAtSection,sectionName,sectionDescription,sectionImageUrl,categoryId);
        this.createdByName = createdByName;
        this.createdById = createdById;
    }

    public VehicleViewDto(Long id, String registrationNumber, Long modelId, String year, FuelType fuelType, Long mileage, TransmissionType transmission, VehicleColor color, String description, String imageUrl, long locationId, Long rateId, String name, Double dailyRate, Double weeklyRate, Double monthlyRate,Long insuranceId,Long sectionId) {
        super(modelId, locationId, rateId,sectionId, registrationNumber, year, fuelType, mileage, transmission, color, description, imageUrl);
        this.id = id;
        this.insuranceId = insuranceId;
    }
}
