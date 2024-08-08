package com.orion.dto.vehicle;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VehicleDashboard {
    private Long totalVehicles;
    private Long availableVehicles;
    private Long rentedVehicles;
    private Long underMaintenance;
    private Long reservedVehicles;
    private Long outOfServiceVehicles;
    private String category;
    private Long categoryCount;
}
