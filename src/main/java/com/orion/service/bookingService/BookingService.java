package com.orion.service.bookingService;
import com.orion.dto.booking.BookingViewDto;
import com.orion.dto.filter.BookingFilter;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
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
    public Page<BookingViewDto> findBookingList(String currentEmail, BookingFilter filter, Integer page, Integer size, String search) {

        Pageable pageable = PageRequest.of(
                page != null ? page - 1 : 1,
                size != null ? size : 10,
                Sort.by("id").descending());

        return repository.findBookingsByCustomer(
                currentEmail,
                filter.getStartDate(),
                filter.getEndDate(),
                filter.getStatus(),
                filter.getVehicleStatus(),
                filter.getVehicleId(),
                filter.getCustomerId(),
                search,
                pageable
        );
    }
    public BookingViewDto findBookingDtoById(Long bookingId) {
        Optional<BookingViewDto> booking = repository.findBookingDto(bookingId);
        isPresent(booking);
        return booking.get();
    }
}
