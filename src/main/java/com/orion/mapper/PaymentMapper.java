package com.orion.mapper;

import com.orion.entity.Payment;
import com.orion.entity.Rental;
import com.orion.entity.Tenant;
import com.orion.enums.payment.PaymentMethod;
import com.orion.enums.payment.PaymentStatus;
import com.orion.service.user.TenantService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.orion.util.TokenUtil.generateTransactionId;

@Service
public class PaymentMapper {
    private final TenantService tenantService;

    public PaymentMapper(TenantService tenantService) {
        this.tenantService = tenantService;
    }

    public Payment createPayment(Rental rental, PaymentMethod paymentMethod, String transactionId) {
        Tenant tenant = tenantService.findById();

        Payment payment = new Payment();
        payment.setRental(rental);
        payment.setAmount(rental.getTotalCost());
        payment.setPaymentMethod(paymentMethod);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setTransactionId(transactionId);
        payment.setStatus(PaymentStatus.PROCESSING);
        payment.setTenant(tenant);
        return payment;
    }
}
