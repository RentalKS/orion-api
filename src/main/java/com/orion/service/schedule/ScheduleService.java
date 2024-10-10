package com.orion.service.schedule;
import com.orion.entity.Payment;
import com.orion.entity.Rental;
import com.orion.enums.vehicle.RentalStatus;
import com.orion.enums.vehicle.VehicleStatus;
import com.orion.repository.PaymentRepository;
import com.orion.repository.RentalRepository;
import com.orion.service.payment.PaymentService;
import com.orion.service.rental.RentalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.SchedulingException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class ScheduleService {
    private final RentalService rentalService;
    @Scheduled(cron = "0 0 * * * ?")
    public void updateVehicleStatus() {
        LocalDateTime now = LocalDateTime.now();
        try {
            List<Rental> rentalsStartingNow = rentalService.findRentalsWaitingToStart();
            log.info("Checking for rentals starting now");
            for (Rental rental : rentalsStartingNow) {
                if (rental.getStartDate().isBefore(now) && now.isBefore(rental.getEndDate())
                        || rental.getStartDate().isEqual(now)) {
                    rentalService.updateRentalStatus(rental, RentalStatus.ONGOING, VehicleStatus.RENTED);
                    log.info("Rental with id {} has started", rental.getId());
                } else if (now.isAfter(rental.getEndDate())) {
                    rentalService.updateRentalStatus(rental, RentalStatus.COMPLETED, VehicleStatus.AVAILABLE);
                    log.info("Rental with id {} has ended", rental.getId());
                }
            }
        } catch (Exception e) {
            log.error("Error occurred while updating vehicle status", e);
            throw new SchedulingException("Error occurred while updating vehicle status", e);
        }

    }

}
