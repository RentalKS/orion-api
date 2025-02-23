package com.orion.service.payment;

import com.orion.dto.PaymentDto;
import com.orion.entity.*;
import com.orion.enums.payment.PaymentStatus;
import com.orion.enums.vehicle.RentalStatus;
import com.orion.enums.vehicle.VehicleStatus;
import com.orion.exception.ErrorCode;
import com.orion.exception.ThrowException;
import com.orion.generics.ResponseObject;
import com.orion.infrastructure.cloudinary.FileUploadService;
import com.orion.mapper.PaymentMapper;
import com.orion.service.BaseService;
import com.orion.service.bookingService.BookingService;
import com.orion.service.notification.NotificationService;
import com.orion.service.rental.RentalService;
import com.orion.util.mail.EmailService;
import com.orion.repository.*;
import com.orion.util.TokenUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.orion.util.TokenUtil.generateTransactionId;

@Service
@RequiredArgsConstructor
@Log4j2
public class PaymentService extends BaseService {
    private static final int TOKEN_EXPIRATION_MINUTES = 1020;
    private final PaymentRepository repository;
    @Autowired
    @Lazy
    private  RentalService rentalService;
    private final EmailService emailService;
    private final PaymentMapper paymentMapper;
    private final BookingService bookingService;
    private final FileUploadService fileUploadService;
    @Autowired
    private NotificationService notificationService;

    @Transactional
    public ResponseObject processPayment(PaymentDto paymentDto, String email) {
        String methodName = "processPayment";
        log.info("Entering: {}", methodName);

        ResponseObject responseObject = new ResponseObject();
        String transactionId = generateTransactionId();
        Rental rental = rentalService.findById(paymentDto.getRentalId());
        validateVehicleStatus(rental.getVehicleStatus());
        Payment payment = paymentMapper.createPayment(rental, paymentDto.getPaymentMethod(),transactionId);

        try {
            payment = sendPaymentConfirmationEmail(payment, rental,transactionId);
            notificationService.sendNotification(email, "Payment Processing", "Customer payment is being processed. Please check your email for the agreement.");
            responseObject.setData("Payment processing. Please check your email for the agreement.");
            responseObject.prepareHttpStatus(HttpStatus.OK);
        } catch (Exception e) {
            agreementEmailFailure(payment, rental.getCustomer().getEmail());
            throw new RuntimeException("Failed to send agreement email. Error: " + e.getMessage());
        }

        log.info("Exiting: {}", methodName);
        return responseObject;
    }

    private void validateVehicleStatus(VehicleStatus status) {
        if (status != VehicleStatus.RESERVED) {
            ThrowException.throwBadRequestApiException(ErrorCode.PAYMENT_STATUS_ERROR, List.of("Vehicle is not reserved."));
        }
    }
    private Payment save(Payment payment) {
        try {
            return this.repository.save(payment);
        } catch (Exception e) {
            log.error("Failed to save payment. Error: {}", e.getMessage());
            return null;
        }
    }

    public Payment sendPaymentConfirmationEmail(Payment payment,Rental rental,String transactionId) {
        String confirmationEmail = rental.getCustomer().getEmail();
        String token = TokenUtil.generateToken(rental.getId(), TOKEN_EXPIRATION_MINUTES, transactionId);
        LocalDateTime expirationTime = TokenUtil.getTokenExpirationTime(TOKEN_EXPIRATION_MINUTES);

        emailService.sendAgreementEmail(confirmationEmail, token, expirationTime);
        log.info("Agreement email sent to {}. Transaction ID: {}", confirmationEmail, transactionId);
        log.info("Token: {}", token);
        payment.setStatus(PaymentStatus.PENDING);
        payment = this.save(payment);

        rentalService.updateRentalStatus(rental, RentalStatus.WAITING_FOR_PAYMENT, VehicleStatus.RESERVED);
        return payment;
    }

    private void agreementEmailFailure(Payment payment, String email) {
        log.error("Failed to send agreement email to {}. Marking payment as FAILED.", email);
        payment.setStatus(PaymentStatus.FAILED);
        this.save(payment);
    }

    @Transactional
    public ResponseObject acceptPayment(PaymentDto paymentDto, String email) {
        String methodName = "validateAndCompleteRental";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Long rentalId = TokenUtil.extractRentalId(paymentDto.getToken());
        String transactionId = TokenUtil.extractTransactionId(paymentDto.getToken());
        Rental rental = rentalService.findById(rentalId);

        isTokenValid(paymentDto.getToken(), rentalId);
        isStatusCompleted(rental.getStatus());

        try {
            rentalService.updateRentalStatus(rental, RentalStatus.WAITING_FOR_START, VehicleStatus.WAITING_TO_START);
            updatePaymentStatus(transactionId, paymentDto.getSignature(),PaymentStatus.SUCCESS);
            notificationService.sendNotification(email, "Rental Agreement Signed", "Customer rental agreement has been signed and completed.");
            responseObject.setData("Rental agreement successfully signed and completed.");
            responseObject.prepareHttpStatus(HttpStatus.OK);

        }catch (Exception e){
            failPayment(rental, transactionId);
            throw new RuntimeException("Failed to complete rental agreement. Error: " + e.getMessage());
        }

        log.info("Exiting: {}", methodName);
        return responseObject;
    }
    private void failPayment(Rental rental, String transactionId) {
        rentalService.updateRentalStatus(rental, RentalStatus.WAITING_FOR_PAYMENT, VehicleStatus.RESERVED);
        updatePaymentStatus(transactionId, null,PaymentStatus.FAILED);
        notificationService.sendNotification(rental.getCustomer().getEmail(), "Rental Agreement Failed", "Your rental agreement has failed to be signed.");
    }
    private void updatePaymentStatus(String transactionId, String signature,PaymentStatus status) {
        Payment payment = findByTransactionId(transactionId);
        String signatureUrl = null;
        if(signature !=null){
            signatureUrl = fileUploadService.uploadSignatureToCloudinary(signature);
        }

        payment.setStatus(status);
        payment.setSignature(signatureUrl);
        this.save(payment);
    }

    private Payment findByTransactionId(String transactionId) {
        Optional<Payment> optionalPayment = repository.findByTransactionId(transactionId);
        isPresent(optionalPayment);
        return optionalPayment.get();
    }

    private void isStatusCompleted(RentalStatus status) {
        if (status == RentalStatus.COMPLETED) {
            ThrowException.throwBadRequestApiException(ErrorCode.BAD_REQUEST, List.of("This rental agreement has already been completed."));
        }
    }

    public void isTokenValid(String token, Long rentalId) {
        boolean isValid = TokenUtil.validateToken(token, rentalId);
        if (!isValid) {
            ThrowException.throwBadRequestApiException(ErrorCode.BAD_REQUEST, List.of("Invalid token."));
        }
    }

    public String findSignaturePaymentByRentalId(String transactionId) {
        return repository.findSignatureByTransactionId(transactionId).orElse(null);
    }

    public String findSignaturePaymentByRentalId(Long rentalId) {
        List<String> signatures = repository.findSignatureByRentalId(rentalId);
        if(!signatures.isEmpty()){
            return signatures.stream().filter(Objects::nonNull).findFirst().orElse(null);
        }
        return null;

    }
}