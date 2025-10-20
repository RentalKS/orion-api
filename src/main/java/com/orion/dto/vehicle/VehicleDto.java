package com.orion.dto.vehicle;

import com.orion.dto.insurancePolicy.InsurancePolicyDto;
import com.orion.enums.vehicle.FuelType;
import com.orion.enums.vehicle.TransmissionType;
import com.orion.enums.vehicle.VehicleColor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class VehicleDto {
    private Long id;
    @NotNull
    private Long modelId;
    @NotNull
    private Long locationId;
    @NotNull
    private Long rateId;
    @NotNull
    private Long sectionId;
    @NotBlank
    private String registrationNumber;
    @NotBlank
    private String year;
    @NotNull
    private FuelType fuelType;
    @NotNull
    private Long mileage;
    @NotNull
    private TransmissionType transmission;
    @NotNull
    private VehicleColor color;
//    @NotBlank
    private MultipartFile image;
    @NotNull
    private InsurancePolicyDto insurancePolicy;

    private String description;
    private String imageUrl;

    public VehicleDto(Long locationId, Long rateId, Long sectionId, String registrationNumber, String year, FuelType fuelType, Long mileage, TransmissionType transmission, VehicleColor color, String description, String imageUrl) {
        this.locationId = locationId;
        this.rateId = rateId;
        this.sectionId = sectionId;
        this.registrationNumber = registrationNumber;
        this.year = year;
        this.fuelType = fuelType;
        this.mileage = mileage;
        this.transmission = transmission;
        this.color = color;
        this.description = description;
        this.imageUrl = imageUrl;
    }


    public VehicleDto(Long id,Long locationId, Long rateId, Long sectionId, String registrationNumber, String year, FuelType fuelType, Long mileage, TransmissionType transmission, VehicleColor color, String description, String imageUrl) {
        this.id = id;
        this.locationId = locationId;
        this.rateId = rateId;
        this.sectionId = sectionId;
        this.registrationNumber = registrationNumber;
        this.year = year;
        this.fuelType = fuelType;
        this.mileage = mileage;
        this.transmission = transmission;
        this.color = color;
        this.description = description;
        this.imageUrl = imageUrl;
    }

}
