package com.orion.controller;

import com.orion.dto.PaymentDto;
import com.orion.enums.payment.PaymentMethod;
import com.orion.generics.ResponseObject;
import com.orion.service.payment.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${base.url}/payment")
@RequiredArgsConstructor
@Log4j2
public class PaymentController {
    private final PaymentService service;

    @PostMapping("/process")
    public ResponseEntity<ResponseObject> processPayment(@RequestBody PaymentDto paymentDto) {
        String methodName = "processPayment";

        log.info("{} -> Process payment", methodName);
        ResponseObject response = service.processPayment(paymentDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/accept")
    public ResponseEntity<ResponseObject> acceptPayment(@RequestBody PaymentDto paymentDto) {
        String methodName = "acceptPayment";

        log.info("{} -> Accept payment", methodName);
        ResponseObject response = service.acceptPayment(paymentDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
