package com.orion.dto.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ModelDto {
    private Long id;
    private String name;
    private Long brandId;
    private String type; // e.g., Sedan, SUV, etc.
    private int seatingCapacity;
    private String fuelEfficiency; // e.g., MPG

    public ModelDto(Long id, String name, Long brandId, String type, int seatingCapacity, String fuelEfficiency){
        this.id = id;
        this.name =name;
        this.brandId = brandId;
        this.type = type;
        this.seatingCapacity = seatingCapacity;
        this.fuelEfficiency = fuelEfficiency;
    }
}
