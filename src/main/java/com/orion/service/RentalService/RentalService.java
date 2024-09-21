package com.orion.service.RentalService;

import com.orion.entity.*;
import com.orion.enums.vehicle.RentalStatus;
import com.orion.enums.vehicle.VehicleStatus;
import com.orion.repository.RentalRepository;
import com.orion.service.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RentalService extends BaseService {
    private final RentalRepository rentalRepository;

    public void createRental(Vehicle vehicle, Customer customer, LocalDateTime startDate, LocalDateTime endDate, Tenant tenant, double totalCost, Booking booking,RentalStatus rentalStatus, VehicleStatus vehicleStatus) {
        Rental rental = new Rental();
        rental.setVehicle(vehicle);
        rental.setCustomer(customer);
        rental.setStartDate(startDate);
        rental.setEndDate(endDate);
        rental.setStatus(rentalStatus);
        rental.setTenant(tenant);
        rental.setTotalCost(totalCost);
        rental.setBooking(booking);
        rental.setVehicleStatus(vehicleStatus);
        rentalRepository.save(rental);
    }
    public void updateRentalStatus(Rental rental, RentalStatus status, VehicleStatus vehicleStatus) {
        rental.setStatus(status);
        rental.setVehicleStatus(vehicleStatus);
        rentalRepository.save(rental);
    }

    public Rental findByBooking(Booking booking) {
        Optional<Rental> optionalRental =  rentalRepository.findByBooking(booking);
        isPresent(optionalRental);
        return optionalRental.get();
    }
}
