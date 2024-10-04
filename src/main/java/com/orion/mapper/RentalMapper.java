package com.orion.mapper;

import com.orion.entity.*;
import com.orion.enums.vehicle.RentalStatus;
import com.orion.enums.vehicle.VehicleStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RentalMapper {
    public Rental toEntity(Vehicle vehicle, Customer customer, LocalDateTime startDate, LocalDateTime endDate, Tenant tenant, double totalCost, Booking booking) {
        Rental rental = new Rental();
        rental.setVehicle(vehicle);
        rental.setCustomer(customer);
        rental.setStartDate(startDate);
        rental.setEndDate(endDate);
        rental.setStatus(RentalStatus.PENDING);
        rental.setTenant(tenant);
        rental.setTotalCost(totalCost);
        rental.setBooking(booking);
        rental.setVehicleStatus(VehicleStatus.RESERVED);
        return rental;
    }
}
