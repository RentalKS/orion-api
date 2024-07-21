package com.orion.dto.maintenanceRecord;

import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.orion.entity.MaintenanceRecord}
 */
@Value
public class MaintenanceRecordDto implements Serializable {
    Long id;
    LocalDateTime createdAt;
    LocalDateTime maintenanceDate;
    String description;
    Double cost;
    Long vehicleId;
}