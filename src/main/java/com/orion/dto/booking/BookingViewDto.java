package com.orion.dto.booking;

import com.orion.enums.vehicle.RentalStatus;
import com.orion.enums.vehicle.VehicleStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class BookingViewDto {
    private Long id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private VehicleStatus bookingStatus;
    private RentalStatus status;
    private Long vehicleId;
    private Long customerId;
}
