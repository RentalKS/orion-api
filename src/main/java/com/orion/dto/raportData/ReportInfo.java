package com.orion.dto.raportData;

import com.orion.enums.vehicle.RentalStatus;
import com.orion.enums.vehicle.VehicleStatus;
import com.orion.util.DateUtil;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReportInfo {
    @NotNull
    private Long from;
    @NotNull
    private Long to;
    private VehicleStatus vehicleStatus;
    private RentalStatus rentalStatus;

    public LocalDateTime getFrom() {
        if(from == null) {
            return null;
        }
        return DateUtil.convertToLocalDateTime(from);
    }

    public LocalDateTime getTo() {
        if(to == null) {
            return null;
        }
        return DateUtil.convertToLocalDateTime(to);
    }
}
