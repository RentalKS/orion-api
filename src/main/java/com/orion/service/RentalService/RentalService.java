package com.orion.service.RentalService;

import com.orion.entity.*;
import com.orion.enums.vehicle.RentalStatus;
import com.orion.repository.RentalRepository;
import com.orion.service.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RentalService extends BaseService {
    private final RentalRepository rentalRepository;

    public void createRental(Vehicle vehicle, Customer customer, LocalDateTime startDate, LocalDateTime endDate, Tenant tenant, double totalCost, Booking booking) {
        Rental rental = new Rental();
        rental.setVehicle(vehicle);
        rental.setCustomer(customer);
        rental.setStartDate(startDate);
        rental.setEndDate(endDate);
        rental.setStatus(RentalStatus.PENDING);
        rental.setTenant(tenant);
        rental.setTotalCost(totalCost);
        rental.setBooking(booking);
        rentalRepository.save(rental);
    }
}
