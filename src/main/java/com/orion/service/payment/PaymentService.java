package com.orion.service.payment;

import com.orion.infrastructure.tenant.ConfigSystem;
import com.orion.entity.*;
import com.orion.enums.payment.PaymentMethod;
import com.orion.enums.payment.PaymentStatus;
import com.orion.enums.vehicle.RentalStatus;
import com.orion.enums.vehicle.VehicleStatus;
import com.orion.generics.ResponseObject;
import com.orion.service.BaseService;
import com.orion.util.mail.EmailService;
import com.orion.repository.*;
import com.orion.util.TokenUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class PaymentService extends BaseService {
    private final PaymentRepository paymentRepository;
    private final RentalRepository rentalRepository;
    private final VehicleRepository vehicleRepository;
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

        Optional<Tenant> tenant = tenantRepository.findTenantById(ConfigSystem.getTenant().getId());
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
            rental.get().setVehicleStatus(VehicleStatus.RESERVED);

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

}