package com.orion.dto.vehicle;

import com.orion.entity.Vehicle;
import com.orion.enums.vehicle.VehicleStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link Vehicle}
 */
@Value
public class VehicleDto implements Serializable {
    Long id;
    LocalDateTime createdAt;
    @NotNull
    String model;
    @NotNull
    String year;
    String price;
    String image;
    String description;
    @NotNull
    VehicleStatus status;
    @NotNull
    String fuelType;
    Long mileage;
    String engine;
    String transmission;
    String color;
    String interior;
    String exterior;
    String vin;
    String stockNumber;
    String mpg;
    String features;
    String options;
    String registrationNumber;
    @NotNull
    Long fleetId;
    @NotNull
    Long locationId;
    @NotNull
    Long sectionId;
}