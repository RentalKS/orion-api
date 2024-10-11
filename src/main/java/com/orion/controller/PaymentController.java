package com.orion.controller;

import com.orion.dto.PaymentDto;
import com.orion.enums.payment.PaymentMethod;
import com.orion.generics.ResponseObject;
import com.orion.service.payment.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Process payment", description = "Process payment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = PaymentDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "404", description = "Files not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @PostMapping("/process")
    public ResponseEntity<ResponseObject> processPayment(@RequestBody PaymentDto paymentDto) {
        String methodName = "processPayment";

        log.info("{} -> Process payment", methodName);
        ResponseObject response = service.processPayment(paymentDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Operation(summary = "Accept payment", description = "Accept payment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = PaymentDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "404", description = "Files not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @PostMapping("/accept")
    public ResponseEntity<ResponseObject> acceptPayment(@RequestBody PaymentDto paymentDto) {
        String methodName = "acceptPayment";

        log.info("{} -> Accept payment", methodName);
        ResponseObject response = service.acceptPayment(paymentDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
