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
import com.orion.service.rental.RentalService;
import com.orion.util.mail.EmailService;
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

import static com.orion.util.TokenUtil.generateTransactionId;

@Service
@RequiredArgsConstructor
@Log4j2
public class PaymentService extends BaseService {
    private static final int TOKEN_EXPIRATION_MINUTES = 5;
    private final PaymentRepository repository;
    private final RentalService rentalService;
    private final EmailService emailService;
    private final PaymentMapper paymentMapper;
    private final FileUploadService fileUploadService;

    @Transactional
    public ResponseObject processPayment(PaymentDto paymentDto) {
        String methodName = "processPayment";
        log.info("Entering: {}", methodName);

        ResponseObject responseObject = new ResponseObject();
        String transactionId = generateTransactionId();
        Rental rental = rentalService.findById(paymentDto.getRentalId());
        Payment payment = paymentMapper.createPayment(rental, paymentDto.getPaymentMethod(),transactionId);

        try {
            payment = sendPaymentConfirmationEmail(payment, rental,transactionId);
            responseObject.setData("Payment processing. Please check your email for the agreement.");
            responseObject.prepareHttpStatus(HttpStatus.OK);
        } catch (Exception e) {
            agreementEmailFailure(payment, rental.getCustomer().getEmail());
            throw new RuntimeException("Failed to send agreement email. Error: " + e.getMessage());
        }

        log.info("Exiting: {}", methodName);
        return responseObject;
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
    public ResponseObject acceptPayment(PaymentDto paymentDto) {
        String methodName = "validateAndCompleteRental";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Long rentalId = TokenUtil.extractRentalId(paymentDto.getToken());
        String transactionId = TokenUtil.extractTransactionId(paymentDto.getToken());
        Rental rental = rentalService.findById(rentalId);

        isTokenValid(paymentDto.getToken(), rentalId);
        isStatusCompleted(rental.getStatus());

        try {
            rentalService.updateRentalStatus(rental, RentalStatus.ONGOING, VehicleStatus.RENTED);
            updatePaymentStatus(transactionId, paymentDto.getSignature());

            responseObject.setData("Rental agreement successfully signed and completed.");
            responseObject.prepareHttpStatus(HttpStatus.OK);

        }catch (Exception e){
            log.error("Failed to complete rental agreement. Error: {}", e.getMessage());
            rentalService.updateRentalStatus(rental, RentalStatus.WAITING_FOR_PAYMENT, VehicleStatus.RESERVED);
            responseObject.setData("Failed to complete rental agreement. Please try again.");
            responseObject.prepareHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        log.info("Exiting: {}", methodName);
        return responseObject;
    }
    private void updatePaymentStatus(String transactionId, String signature) {
        Payment payment = findByTransactionId(transactionId);
        payment.setStatus(PaymentStatus.SUCCESS);
        String signatureUrl = fileUploadService.uploadSignatureToCloudinary(signature);
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
}