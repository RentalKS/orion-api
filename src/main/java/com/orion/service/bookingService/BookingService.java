package com.orion.service.bookingService;

import com.orion.entity.Booking;
import com.orion.entity.Customer;
import com.orion.entity.Tenant;
import com.orion.entity.Vehicle;
import com.orion.enums.vehicle.RentalStatus;
import com.orion.enums.vehicle.VehicleStatus;
import com.orion.mapper.BookingMapper;
import com.orion.repository.BookingRepository;
import com.orion.service.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingService extends BaseService {
    private final BookingRepository repository;
    private final BookingMapper bookingMapper;

    public Booking createBooking(Vehicle vehicle, Customer customer, LocalDateTime startDate, LocalDateTime endDate, Tenant tenant) {
            Booking booking = bookingMapper.toEntity(vehicle, customer, startDate, endDate, tenant);
            repository.save(booking);
            return booking;
    }

    public Boolean findBookingsByVehicleAndDateRange(Long vehicleId, LocalDateTime startDate, LocalDateTime endDate) {
       return repository.findBookingsByVehicleAndDateRange(vehicleId, startDate, endDate);
    }
    public Booking findBookingById(Long bookingId) {
        Optional<Booking> booking = repository.findById(bookingId);
        isPresent(booking);
        return booking.get();
    }
    public void updateBookingStatus(Booking booking, RentalStatus status, VehicleStatus vehicleStatus) {
        booking.setStatus(status);
        booking.setBookingStatus(vehicleStatus);
        repository.save(booking);
    }


}
