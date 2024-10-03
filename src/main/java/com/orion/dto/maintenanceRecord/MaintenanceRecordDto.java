package com.orion.dto.maintenanceRecord;

import com.orion.enums.vehicle.DamageType;
import com.orion.util.DateUtil;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.orion.entity.MaintenanceRecord}
 */
@Getter
@Setter
@NoArgsConstructor
public class MaintenanceRecordDto implements Serializable {
    private Long id;
    @NotBlank
    private Long maintenanceStartDate;
    private Long maintenanceEndDate;
    private String description;
    @NotNull
    private Double cost;
    @NotBlank
    private DamageType damageType;
    private Long vehicleId;

    public MaintenanceRecordDto(Long id, LocalDateTime maintenanceStartDate,LocalDateTime maintenanceEndDate, String description, Double cost,DamageType damageType, Long vehicleId) {
        this.id = id;
        this.maintenanceStartDate = DateUtil.localDateTimeToMilliseconds(maintenanceStartDate);
        this.maintenanceEndDate = DateUtil.localDateTimeToMilliseconds(maintenanceEndDate);
        this.description = description;
        this.cost = cost;
        this.damageType = damageType;
        this.vehicleId = vehicleId;
    }
}