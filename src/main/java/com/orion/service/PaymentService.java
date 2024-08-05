package com.orion.service;

import com.orion.config.tenant.TenantContext;
import com.orion.entity.*;
import com.orion.enums.payment.PaymentMethod;
import com.orion.enums.payment.PaymentStatus;
import com.orion.enums.vehicle.RentalStatus;
import com.orion.enums.vehicle.VehicleStatus;
import com.orion.generics.ResponseObject;
import com.orion.mail.EmailService;
import com.orion.repository.*;
import com.orion.util.TokenUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class PaymentService extends BaseService {
    private final PaymentRepository paymentRepository;
    private final RentalRepository rentalRepository;
    private final VehicleRepository vehicleRepository;
    private final BookingRepository bookingRepository;
    private final EmailService emailService;
    private final TenantRepository tenantRepository;

    private static final int TOKEN_EXPIRATION_MINUTES = 5;

    @Transactional
    public ResponseObject processPayment(Long rentalId, String paymentMethod) {
        String methodName = "processPayment";
        log.info("Entering: {}", methodName);

        ResponseObject responseObject = new ResponseObject();

        Optional<Rental> rental = rentalRepository.findById(rentalId);
        isPresent(rental);

        Optional<Tenant> tenant = tenantRepository.findTenantById(TenantContext.getCurrentTenant().getId());
        isPresent(tenant);

        Payment payment = new Payment();
        payment.setRental(rental.get());
        payment.setAmount(rental.get().getTotalCost());
        payment.setPaymentMethod(PaymentMethod.valueOf(paymentMethod));
        payment.setPaymentDate(LocalDateTime.now());
        payment.setTransactionId(generateTransactionId());
        payment.setStatus(PaymentStatus.PROCESSING);
        payment.setTenant(tenant.get());

        try {

            String token = TokenUtil.generateToken(rentalId, TOKEN_EXPIRATION_MINUTES);
            LocalDateTime expirationTime = TokenUtil.getTokenExpirationTime(TOKEN_EXPIRATION_MINUTES);
            emailService.sendAgreementEmail(rental.get().getCustomer().getEmail(), token, expirationTime);

            payment.setStatus(PaymentStatus.PENDING);
            rental.get().setStatus(RentalStatus.WAITING_FOR_PAYMENT);
            rental.get().getVehicle().setStatus(VehicleStatus.RESERVED);

            rentalRepository.save(rental.get());
            vehicleRepository.save(rental.get().getVehicle());

            responseObject.setData(payment);
            responseObject.prepareHttpStatus(HttpStatus.OK);
        } catch (Exception e) {
            log.error("Failed to send agreement email. Marking payment as FAILED. Error: {}", e.getMessage());
            payment.setStatus(PaymentStatus.FAILED);
            responseObject.setData("Payment succeeded but email failed to send. Please contact support.");
            responseObject.prepareHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            paymentRepository.save(payment);
            return responseObject;
        }

        paymentRepository.save(payment);
        log.info("Exiting: {}", methodName);
        return responseObject;
    }

    private String generateTransactionId() {
        return UUID.randomUUID().toString();
    }

    @Transactional
    public ResponseObject modifyReservation(Long bookingId, LocalDateTime newStartDate, LocalDateTime newEndDate) {
        String methodName = "modifyReservation";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Optional<Booking> booking = bookingRepository.findById(bookingId);
        isPresent(booking);

        Booking existingBooking = booking.get();
        if (existingBooking.getStatus() != RentalStatus.PENDING) {
            responseObject.prepareHttpStatus(HttpStatus.BAD_REQUEST);
            responseObject.setData("Only pending reservations can be modified.");
            return responseObject;
        }

        existingBooking.setStartDate(newStartDate);
        existingBooking.setEndDate(newEndDate);

        boolean isAvailable = checkVehicleAvailability(existingBooking.getVehicle(), newStartDate, newEndDate);
        if (!isAvailable) {
            responseObject.prepareHttpStatus(HttpStatus.BAD_REQUEST);
            responseObject.setData("Vehicle is not available for the new selected dates");
            return responseObject;
        }

        bookingRepository.save(existingBooking);

        responseObject.setData(existingBooking);
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }

    public ResponseObject checkAvailability(Long vehicleId, LocalDateTime startDate, LocalDateTime endDate) {
        String methodName = "checkAvailability";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Optional<Vehicle> vehicle = vehicleRepository.findById(vehicleId);
        isPresent(vehicle);

        boolean isAvailable = checkVehicleAvailability(vehicle.get(), startDate, endDate);
        responseObject.setData(isAvailable);
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }

    private boolean checkVehicleAvailability(Vehicle vehicle, LocalDateTime startDate, LocalDateTime endDate) {
        List<Booking> bookings = bookingRepository.findBookingsByVehicleAndDateRange(vehicle.getId(), startDate, endDate);
        return bookings.isEmpty();
    }

}