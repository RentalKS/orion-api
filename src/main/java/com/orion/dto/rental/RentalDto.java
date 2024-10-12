package com.orion.dto.rental;

import com.orion.dto.customer.CustomerDto;
import com.orion.enums.vehicle.RentalStatus;
import com.orion.enums.vehicle.VehicleStatus;
import com.orion.util.DateUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
public class RentalDto {
    private Long id;
    private Long startDate;
    private Long endDate;
    private RentalStatus status;
    private double totalCost;
    private Long vehicleId;
    private Long totalDays;
    private CustomerDto customerDetails;
    private VehicleStatus vehicleStatus;

    public RentalDto(Long id, LocalDateTime startDate, LocalDateTime endDate, RentalStatus status, double totalCost, Long vehicleId, Long customerId, String name, String lastName, String email, String phoneNumber, String licenseNumber, String contactAgent, VehicleStatus vehicleStatus) {
        this.id = id;
        this.startDate = DateUtil.localDateTimeToMilliseconds(startDate);
        this.endDate = DateUtil.localDateTimeToMilliseconds(endDate);
        this.status = status;
        this.totalCost = totalCost;
        this.vehicleId = vehicleId;
        this.vehicleStatus = vehicleStatus;
        this.customerDetails = new CustomerDto(customerId, name, lastName, email, phoneNumber, licenseNumber, contactAgent);
        this.totalDays = ChronoUnit.DAYS.between(startDate, endDate);
    }
    public RentalDto(Long id, LocalDateTime startDate, LocalDateTime endDate, RentalStatus status, double totalCost, Long vehicleId, VehicleStatus vehicleStatus) {
        this.id = id;
        this.startDate = DateUtil.localDateTimeToMilliseconds(startDate);
        this.endDate = DateUtil.localDateTimeToMilliseconds(endDate);
        this.status = status;
        this.totalCost = totalCost;
        this.vehicleId = vehicleId;
        this.vehicleStatus = vehicleStatus;
        this.totalDays = ChronoUnit.DAYS.between(startDate, endDate);
    }

}
