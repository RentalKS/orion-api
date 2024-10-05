package com.orion.service.rental;

import com.orion.dto.rental.RentalDto;
import com.orion.entity.*;
import com.orion.enums.vehicle.RentalStatus;
import com.orion.enums.vehicle.VehicleStatus;
import com.orion.mapper.RentalMapper;
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
    private final RentalMapper rentalMapper;

    public void createRental(Vehicle vehicle, Customer customer, LocalDateTime startDate, LocalDateTime endDate, Tenant tenant, double totalCost, Booking booking) {
        Rental rental = rentalMapper.toEntity(vehicle, customer, startDate, endDate, tenant, totalCost, booking);
        rentalRepository.save(rental);
    }
    public void updateRentalStatus(Rental rental, RentalStatus status, VehicleStatus vehicleStatus) {
        rental.setStatus(status);
        rental.setVehicleStatus(vehicleStatus);
        rentalRepository.save(rental);
    }

    public Rental findByBooking(Long bookingId) {
        Optional<Rental> optionalRental =  rentalRepository.findByBooking(bookingId);
        isPresent(optionalRental);
        return optionalRental.get();
    }
    public RentalDto findByBookingDto(Long bookingId) {
        Optional<RentalDto> optionalRental =  rentalRepository.findByBookingDto(bookingId);
        isPresent(optionalRental);
        return optionalRental.get();
    }
}
