package com.orion.dto.vehicle;

import com.orion.enums.vehicle.VehicleStatus;
import com.orion.enums.vehicle.FuelType;
import com.orion.enums.vehicle.TransmissionType;
import com.orion.entity.Model;
import com.orion.entity.Location;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class VehicleDto {
    private Long createdAt;;
    private Long id;
    private String registrationNumber;
    private Long modelId;
    private String modelName;
    private String modelBrand;
    private String modelType;
    private String modelImage;
    private String locationName;
    private String locationAddress;
    private String year;
    private VehicleStatus status;
    private FuelType fuelType;
    private Long mileage;
    private TransmissionType transmission;
    private String color;
    private String description;
    private String image;
    private Long locationId;
    private Long rateId;
    private String rateName;
    private Double dailyRate;
    private Double weeklyRate;
    private Double monthlyRate;
    private Long sectionId;
    private String sectionName;


    public VehicleDto(Long id, String registrationNumber, Long modelId, String year, VehicleStatus status, FuelType fuelType, Long mileage, TransmissionType transmission, String color, String description, String image, long locationId, Long rateId, String name,Double dailyRate, Double weeklyRate, Double monthlyRate) {
        this.id = id;
        this.registrationNumber = registrationNumber;
        this.modelId = modelId;
        this.year = year;
        this.status = status;
        this.fuelType = fuelType;
        this.mileage = mileage;
        this.transmission = transmission;
        this.color = color;
        this.description = description;
        this.image = image;
        this.locationId = locationId;
        this.rateId = rateId;
        this.rateName = name;
        this.weeklyRate = weeklyRate;
        this.dailyRate = dailyRate;
        this.monthlyRate = monthlyRate;

    }

}
