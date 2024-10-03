package com.orion.dto.vehicle;

import com.orion.dto.insurancePolicy.InsurancePolicyDto;
import com.orion.enums.vehicle.FuelType;
import com.orion.enums.vehicle.TransmissionType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class VehicleCreateDto {
    private Long modelId;
    private Long locationId;
    private Long rateId;
    private String registrationNumber;
    private String year;
    private FuelType fuelType;
    private Long mileage;
    private TransmissionType transmission;
    private String color;
    private String description;
    private String imageUrl;
    private MultipartFile image;
    private InsurancePolicyDto insurancePolicyDto;
}
