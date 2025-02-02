package com.orion.dto.booking;

import com.orion.dto.customer.CustomerDto;
import com.orion.enums.vehicle.RentalStatus;
import com.orion.enums.vehicle.VehicleStatus;
import com.orion.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter

public class BookingViewDto {
    private Long id;
    private Long startDate;
    private Long endDate;
    private VehicleStatus bookingStatus;
    private RentalStatus status;
    private Long vehicleId;
    private CustomerDto customer;

    public BookingViewDto(Long id, LocalDateTime startDate, LocalDateTime endDate, VehicleStatus bookingStatus, RentalStatus status, Long vehicleId, Long customerId) {
        this.id = id;
        this.startDate = DateUtil.localDateTimeToMilliseconds(startDate);
        this.endDate = DateUtil.localDateTimeToMilliseconds(endDate);
        this.bookingStatus = bookingStatus;
        this.status = status;
        this.vehicleId = vehicleId;
    }

    public BookingViewDto(Long id, LocalDateTime startDate, LocalDateTime endDate, Long vehicleId, Long customerId, String name, String lastName, String email, String phoneNumber, String licenseNumber, String contactAgent) {
        this.id = id;
        this.startDate = DateUtil.localDateTimeToMilliseconds(startDate);
        this.endDate = DateUtil.localDateTimeToMilliseconds(endDate);
        this.vehicleId = vehicleId;
        this.customer = new CustomerDto(customerId, name, lastName, email, phoneNumber, licenseNumber, contactAgent);
    }
}
