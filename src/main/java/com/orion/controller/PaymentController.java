package com.orion.controller;

import com.orion.generics.ResponseObject;
import com.orion.service.payment.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${base.url}/payment")
@RequiredArgsConstructor
@Log4j2
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/process/{rentalId}/{paymentMethod}")
    public ResponseEntity<ResponseObject> processPayment(@PathVariable Long rentalId, @PathVariable String paymentMethod) {
        String methodName = "processPayment";

        log.info("{} -> Process payment", methodName);
        ResponseObject response = paymentService.processPayment(rentalId, paymentMethod);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
