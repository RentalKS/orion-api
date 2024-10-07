package com.orion.service;

import com.orion.dto.booking.BookingViewDto;
import com.orion.dto.filter.BookingFilter;
import com.orion.dto.rental.RentalDto;
import com.orion.dto.reservation.ReservationDto;
import com.orion.dto.vehicle.Available;
import com.orion.entity.*;
import com.orion.enums.vehicle.RentalStatus;
import com.orion.enums.vehicle.VehicleStatus;
import com.orion.exception.ErrorCode;
import com.orion.exception.ThrowException;
import com.orion.generics.ResponseObject;
import com.orion.service.bookingService.BookingService;
import com.orion.service.customer.CustomerService;
import com.orion.service.notification.NotificationService;
import com.orion.service.rental.RentalService;
import com.orion.service.user.TenantService;
import com.orion.service.vehicle.VehicleService;
import com.orion.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class ReservationService extends BaseService {
    private final static String reservationConfirmation = "Reservation Confirmation";
    private final static String reservationConfirmed = "Your reservation has been confirmed.";

    private final VehicleService vehicleService;
    private final BookingService bookingService;
    private final RentalService rentalService;
    private final CustomerService customerService;
    private final TenantService tenantService;
    private final NotificationService notificationService;
    @Transactional
    public ResponseObject create(ReservationDto reservationDto) {
        String methodName = "createReservation";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Tenant tenant = tenantService.findById();
        Vehicle vehicle = vehicleService.findById(reservationDto.getVehicleId());

        LocalDateTime startDate =  DateUtil.convertToLocalDateTime(reservationDto.getStartDate());
        LocalDateTime endDate = DateUtil.convertToLocalDateTime(reservationDto.getEndDate());
        Customer customer = customerService.findById(reservationDto.getCustomerId());

        Boolean isNotAvailable = checkVehicleAvailability(vehicle,startDate,endDate);

        if (Boolean.TRUE.equals(isNotAvailable)) {
            responseObject.prepareHttpStatus(HttpStatus.BAD_REQUEST);
            ThrowException.throwBadRequestApiException(ErrorCode.VEHICLE_NOT_AVAILABLE, List.of("Vehicle is not available for the selected dates."));
            return responseObject;
        }

        try {

            long rentalDays = ChronoUnit.DAYS.between(startDate, endDate);
            double totalCost = calculateTotalCost(vehicle.getRateDates(), rentalDays);

            Booking booking = bookingService.createBooking(vehicle, customer, startDate, endDate, tenant);
            rentalService.createRental(vehicle, customer, startDate, endDate, tenant, totalCost, booking);
            notificationService.sendNotification(customer.getId(), reservationConfirmation, reservationConfirmed);

            responseObject.setData(booking.getId());
            responseObject.prepareHttpStatus(HttpStatus.CREATED);
        }catch (Exception e) {
            log.error("Error creating reservation: {}", e.getMessage());
            throw new RuntimeException(e.getLocalizedMessage());
        }
        return responseObject;
    }
    private Boolean checkVehicleAvailability(Vehicle vehicle, LocalDateTime startDate, LocalDateTime endDate) {
        Boolean bookingAvailable =  bookingService.findBookingsByVehicleAndDateRange(vehicle.getId(), startDate, endDate);
        Boolean isVehicleOnMaintenance = vehicleService.isVehicleOnMaintenance(vehicle.getId(), startDate, endDate);
        return bookingAvailable || isVehicleOnMaintenance;
    }
    private double calculateTotalCost(RateDates rateDates, long rentalDays) {
        DecimalFormat df = new DecimalFormat("#.##");

        double dailyRate = rateDates.getDailyRate();
        double weeklyRate = rateDates.getWeeklyRate();
        double monthlyRate = rateDates.getMonthlyRate();
        double totalCost;

        if (rentalDays >= 7 && rentalDays < 30) {
            totalCost = weeklyRate * (rentalDays / 7.0);
        } else if (rentalDays >= 30) {
            totalCost = monthlyRate * (rentalDays / 30.0);
        } else {
            totalCost = dailyRate * rentalDays;
        }

        totalCost += applyDiscounts(totalCost);
        return Double.parseDouble(df.format(totalCost));
    }
    private double applyDiscounts(double totalCost) {
        double discountPercentage = 0.10;
        return totalCost * discountPercentage;
    }
    @Transactional
    public ResponseObject cancelReservation(Long bookingId) {
        String methodName = "cancelReservation";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Booking booking = bookingService.findBookingById(bookingId);

        if (booking.getStatus() != RentalStatus.PENDING) {
            responseObject.prepareHttpStatus(HttpStatus.BAD_REQUEST);
            responseObject.setData("Only pending reservations can be cancelled.");
            return responseObject;
        }

        bookingService.updateBookingStatus(booking, RentalStatus.CANCELLED, VehicleStatus.AVAILABLE);
        Rental rental = rentalService.findByBooking(booking.getId());
        rentalService.updateRentalStatus(rental, RentalStatus.CANCELLED, VehicleStatus.AVAILABLE);


        responseObject.setData(booking);
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }
    public ResponseObject checkAvailability(Available available) {
        String methodName = "checkAvailability";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Vehicle vehicle = vehicleService.findById(available.getVehicleId());

        boolean isNotAvailable = checkVehicleAvailability(vehicle, available.getStartDate(), available.getEndDate());
        responseObject.setData(!isNotAvailable);
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }

    public ResponseObject getReservations(String currentEmail, BookingFilter filter, Integer page, Integer size,String search) {
        String methodName = "getReservations";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        try {
            Page<BookingViewDto> bookings = bookingService.findBookingList(currentEmail,filter,page, size,search);

            responseObject.setData(mapPage(bookings));
            responseObject.prepareHttpStatus(HttpStatus.OK);
        }catch (Exception e) {
            log.error("Error getting reservations: {}", e.getMessage());
            throw new RuntimeException(e.getLocalizedMessage());
        }
        return responseObject;
    }

    public ResponseObject getReservationFromBookingId(Long bookingId) {
        String methodName = "getReservation";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();
        BookingViewDto booking = bookingService.findBookingDtoById(bookingId);

        responseObject.setData(booking);
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }

    public ResponseObject getDetails(Long bookingId) {
        String methodName = "details";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();
        try {
            RentalDto rental = rentalService.findByBookingDto(bookingId);
            responseObject.setData(rental);
            responseObject.prepareHttpStatus(HttpStatus.OK);
        }catch (Exception e) {
            log.error("Error getting reservation details: {}", e.getMessage());
            throw new RuntimeException(e.getLocalizedMessage());
        }
        return responseObject;
    }
}
