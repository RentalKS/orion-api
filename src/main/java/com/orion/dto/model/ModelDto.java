package com.orion.dto.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ModelDto {
    private Long id;
    private String name;
    private String brand;
    private String type; // e.g., Sedan, SUV, etc.
    private int seatingCapacity;
    private String fuelEfficiency; // e.g., MPG
}
