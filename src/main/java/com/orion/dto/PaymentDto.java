package com.orion.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.orion.enums.payment.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentDto {
    private Long rentalId;
    private PaymentMethod paymentMethod;
    @NotBlank
    private String signature;
    @NotBlank
    private String token;
}
