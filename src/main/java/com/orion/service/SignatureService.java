package com.orion.service;

import com.orion.entity.Payment;
import com.orion.entity.Rental;
import com.orion.enums.payment.PaymentStatus;
import com.orion.enums.vehicle.RentalStatus;
import com.orion.enums.vehicle.VehicleStatus;
import com.orion.exception.ErrorCode;
import com.orion.exception.ThrowException;
import com.orion.generics.ResponseObject;
import com.orion.repository.PaymentRepository;
import com.orion.repository.RentalRepository;
import com.orion.util.TokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SignatureService extends BaseService{

    private final RentalRepository rentalRepository;
    private final PaymentRepository paymentRepository;

    @Transactional
    public ResponseObject validateAndCompleteRental(String token) {
        String methodName = "validateAndCompleteRental";
        log.info("Entering: {}", methodName);

        ResponseObject responseObject = new ResponseObject();

        boolean isValid = TokenUtil.validateToken(token);
        if (!isValid) {
            ThrowException.throwBadRequestApiException(ErrorCode.BAD_REQUEST, List.of("Invalid token."));
            responseObject.prepareHttpStatus(HttpStatus.BAD_REQUEST);
            return responseObject;
        }

        Long rentalId = TokenUtil.extractRentalId(token);
        Optional<Rental> rental = rentalRepository.findRentalWaitingToPayment(rentalId);
        isPresent(rental);


        if (rental.get().getStatus() == RentalStatus.COMPLETED) {
            ThrowException.throwBadRequestApiException(ErrorCode.BAD_REQUEST, List.of("This rental agreement has already been completed."));
            responseObject.prepareHttpStatus(HttpStatus.BAD_REQUEST);
            return responseObject;
        }

        rental.get().setStatus(RentalStatus.ONGOING);
        rental.get().getVehicle().setStatus(VehicleStatus.RENTED);

        rentalRepository.save(rental.get());
        paymentRepository.updatePaymentStatusByRentalId(rentalId, PaymentStatus.SUCCESS);

        responseObject.setData("Rental agreement successfully signed and completed.");
        responseObject.prepareHttpStatus(HttpStatus.OK);

        log.info("Exiting: {}", methodName);
        return responseObject;
    }
}
