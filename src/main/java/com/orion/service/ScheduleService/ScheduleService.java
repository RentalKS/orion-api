package com.orion.service.ScheduleService;

import com.orion.entity.Booking;
import com.orion.entity.Rental;
import com.orion.entity.Vehicle;
import com.orion.enums.vehicle.RentalStatus;
import com.orion.enums.vehicle.VehicleStatus;
import com.orion.repository.BookingRepository;
import com.orion.repository.RentalRepository;
import com.orion.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class ScheduleService {
    private final RentalRepository rentalRepository;
    private final VehicleRepository vehicleRepository;
    private final BookingRepository bookingRepository;


    @Scheduled(cron = "0 0 * * * ?")
    public void updateVehicleStatus() {
        LocalDateTime now = LocalDateTime.now();
        List<Rental> rentalsStartingNow = rentalRepository.findRentalsStartingAt(now);
        for (Rental rental : rentalsStartingNow) {
            Vehicle vehicle = rental.getVehicle();
            vehicle.setStatus(VehicleStatus.RENTED);
            vehicleRepository.save(vehicle);
        }
    }
        @Scheduled(cron = "0 0 0 * * *")
    public void updateRentalStatuses() {
        log.info("Scheduled task: Updating rental statuses based on end dates.");
        List<Booking> bookings = bookingRepository.findByStatus(RentalStatus.ONGOING);

        bookings.forEach(booking -> {
            if (booking.getEndDate().isBefore(LocalDateTime.now())) {
                booking.setStatus(RentalStatus.COMPLETED);
                booking.getVehicle().setStatus(VehicleStatus.AVAILABLE);
                bookingRepository.save(booking);
                vehicleRepository.save(booking.getVehicle());
            }
        });
    }

}
