package com.orion.dto.rental;

import com.orion.dto.customer.CustomerDto;
import com.orion.enums.vehicle.RentalStatus;
import com.orion.util.DateUtil;
import lombok.Getter;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter
public class RentalDto {
    private final Long id;
    private final Long startDate;
    private final Long endDate;
    private final RentalStatus status;
    private final double totalCost;
    private final Long vehicleId;
    private final Long totalDays;
    private final CustomerDto customerDetails;

    public RentalDto(Long id, LocalDateTime startDate, LocalDateTime endDate, RentalStatus status, double totalCost, Long vehicleId,Long customerId, String name, String lastName, String email, String phoneNumber, String licenseNumber, String contactAgent) {
        this.id = id;
        this.startDate = DateUtil.localDateTimeToMilliseconds(startDate);
        this.endDate = DateUtil.localDateTimeToMilliseconds(endDate);
        this.status = status;
        this.totalCost = totalCost;
        this.vehicleId = vehicleId;
        this.customerDetails = new CustomerDto(customerId, name, lastName, email, phoneNumber, licenseNumber, contactAgent);
        this.totalDays = ChronoUnit.DAYS.between(startDate, endDate);
    }

}
