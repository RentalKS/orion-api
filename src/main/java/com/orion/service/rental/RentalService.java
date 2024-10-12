package com.orion.service.rental;

import com.orion.dto.rental.RentalDto;
import com.orion.entity.*;
import com.orion.enums.vehicle.RentalStatus;
import com.orion.enums.vehicle.VehicleStatus;
import com.orion.infrastructure.tenant.ConfigSystem;
import com.orion.mapper.RentalMapper;
import com.orion.repository.RentalRepository;
import com.orion.service.BaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class RentalService extends BaseService {
    private final RentalRepository repository;
    private final RentalMapper rentalMapper;

    public void createRental(Vehicle vehicle, Customer customer, LocalDateTime startDate, LocalDateTime endDate, Tenant tenant, double totalCost, Booking booking) {
        Rental rental = rentalMapper.toEntity(vehicle, customer, startDate, endDate, tenant, totalCost, booking);
        this.save(rental);
    }
    public void updateRentalStatus(Rental rental, RentalStatus status, VehicleStatus vehicleStatus) {
        rental.setStatus(status);
        rental.setVehicleStatus(vehicleStatus);
        this.save(rental);
    }

    public Rental findByBooking(Long bookingId) {
        Optional<Rental> optionalRental =  repository.findByBooking(bookingId);
        isPresent(optionalRental);
        return optionalRental.get();
    }
    public RentalDto findByBookingDto(Long bookingId) {
        Optional<RentalDto> optionalRental =  repository.findByBookingDto(bookingId);
        isPresent(optionalRental);
        return optionalRental.get();
    }

    public Rental findById(Long rentalId) {
        Optional<Rental> optionalRental = repository.findById(rentalId, ConfigSystem.getTenant().getId());
        isPresent(optionalRental);
        return optionalRental.get();
    }
    private Rental save(Rental rental) {
        try {
            return this.repository.save(rental);
        } catch (Exception e) {
            log.error("Failed to save rental. Error: {}", e.getMessage());
            throw new RuntimeException("Failed to save rental",e.getCause());
        }
    }
    public List<Rental> findRentalsWaitingToStart(){
        List<Rental> rentalsStartingNow = repository.findRentalsWaitingToStart(RentalStatus.WAITING_FOR_START,VehicleStatus.WAITING_TO_START);
        return Optional.ofNullable(rentalsStartingNow).orElse(List.of());
    }

    public List<RentalDto> findCustomerRentals(Long customerId) {
        List<RentalDto> rentals = repository.findCustomerRentals(customerId);
        return Optional.ofNullable(rentals).orElse(List.of());
    }
}
