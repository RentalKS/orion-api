package com.orion.dto.filter;

import com.orion.enums.vehicle.RentalStatus;
import com.orion.enums.vehicle.VehicleStatus;
import com.orion.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class BookingFilter {
    private Long startDate;
    private Long endDate;
    private RentalStatus status;
    private VehicleStatus vehicleStatus;
    private String vehicleId;
    private String customerId;
    public LocalDateTime getStartDate() {
        if(startDate == null) {
            return null;
        }
        return DateUtil.convertToLocalDateTime(startDate);
    }

    public LocalDateTime getEndDate() {
        if(endDate == null) {
            return null;
        }
        return DateUtil.convertToLocalDateTime(endDate);
    }

}
