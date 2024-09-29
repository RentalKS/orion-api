package com.orion.service.ScheduleService;
import com.orion.entity.Rental;
import com.orion.enums.vehicle.RentalStatus;
import com.orion.enums.vehicle.VehicleStatus;
import com.orion.repository.RentalRepository;
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

    @Scheduled(cron = "0 0 * * * ?")
    public void updateVehicleStatus() {
        LocalDateTime now = LocalDateTime.now();
        List<Rental> rentalsStartingNow = rentalRepository.findRentalsWaitingToStart(VehicleStatus.RESERVED);
        for (Rental rental : rentalsStartingNow) {

            if(now.isAfter(rental.getEndDate())){
                rental = updateRentalStatus(rental, VehicleStatus.AVAILABLE, RentalStatus.COMPLETED);
            }else {
                rental = updateRentalStatus(rental, VehicleStatus.RENTED, RentalStatus.ONGOING);
            }
            rentalRepository.save(rental);
        }
    }

    public Rental updateRentalStatus(Rental rental, VehicleStatus vehicleStatus,RentalStatus status) {
        rental.setVehicleStatus(vehicleStatus);
        rental.setStatus(status);
        rental.getBooking().setBookingStatus(vehicleStatus);
        rental.getBooking().setStatus(status);
        return rental;
    }

}
