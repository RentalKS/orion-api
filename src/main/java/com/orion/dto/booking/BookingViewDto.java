package com.orion.dto.booking;

import com.orion.enums.vehicle.RentalStatus;
import com.orion.enums.vehicle.VehicleStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter

public class BookingViewDto {
    private Long id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private VehicleStatus bookingStatus;
    private RentalStatus status;
    private Long vehicleId;
    private Long customerId;

    public BookingViewDto(Long id, LocalDateTime startDate, LocalDateTime endDate, VehicleStatus bookingStatus, RentalStatus status, Long vehicleId, Long customerId) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.bookingStatus = bookingStatus;
        this.status = status;
        this.vehicleId = vehicleId;
        this.customerId = customerId;
    }

    public BookingViewDto(Long id, LocalDateTime startDate, LocalDateTime endDate, Long vehicleId, Long customerId) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.vehicleId = vehicleId;
        this.customerId = customerId;
    }
}
