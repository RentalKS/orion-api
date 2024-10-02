package com.orion.controller;

import com.orion.generics.ResponseObject;
import com.orion.service.payment.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${base.url}/payment")
@RequiredArgsConstructor
@Log4j2
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/process")
    public ResponseEntity<ResponseObject> processPayment(@RequestParam Long rentalId, @RequestParam String paymentMethod) {
        String methodName = "processPayment";

        log.info("{} -> Process payment", methodName);
        ResponseObject response = paymentService.processPayment(rentalId, paymentMethod);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
