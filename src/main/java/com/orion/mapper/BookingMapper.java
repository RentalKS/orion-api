package com.orion.mapper;

import com.orion.entity.Booking;
import com.orion.entity.Customer;
import com.orion.entity.Tenant;
import com.orion.entity.Vehicle;
import com.orion.enums.vehicle.VehicleStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BookingMapper {
    public Booking toEntity(Vehicle vehicle, Customer customer, LocalDateTime startDate, LocalDateTime endDate, Tenant tenant) {
        Booking booking = new Booking();
        booking.setVehicle(vehicle);
        booking.setCustomer(customer);
        booking.setStartDate(startDate);
        booking.setEndDate(endDate);
        booking.setBookingStatus(VehicleStatus.RESERVED);
        booking.setTenant(tenant);
        return booking;
    }
}
