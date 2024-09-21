package com.orion.service.BookingService;

import com.orion.entity.Booking;
import com.orion.entity.Customer;
import com.orion.entity.Tenant;
import com.orion.entity.Vehicle;
import com.orion.enums.vehicle.RentalStatus;
import com.orion.enums.vehicle.VehicleStatus;
import com.orion.generics.ResponseObject;
import com.orion.repository.BookingRepository;
import com.orion.service.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingService extends BaseService {
    private final BookingRepository bookingRepository;

    public Booking createBooking(Vehicle vehicle, Customer customer, LocalDateTime startDate, LocalDateTime endDate, Tenant tenant,VehicleStatus vehicleStatus) {
            Booking booking = new Booking();
            booking.setVehicle(vehicle);
            booking.setCustomer(customer);
            booking.setStartDate(startDate);
            booking.setEndDate(endDate);
            booking.setBookingStatus(vehicleStatus);
            booking.setTenant(tenant);
            bookingRepository.save(booking);
            return booking;
    }

    public boolean findBookingsByVehicleAndDateRange(Long vehicleId, LocalDateTime startDate, LocalDateTime endDate) {
        return bookingRepository.findBookingsByVehicleAndDateRange(vehicleId, startDate, endDate);
    }
    public Booking findBookingById(Long bookingId) {
        Optional<Booking> booking = bookingRepository.findById(bookingId);
        isPresent(booking);
        return booking.get();
    }
    public void updateBookingStatus(Booking booking, RentalStatus status, VehicleStatus vehicleStatus) {
        booking.setStatus(status);
        booking.setBookingStatus(vehicleStatus);
        bookingRepository.save(booking);
    }


}
