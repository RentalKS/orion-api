package com.orion.controller;

import com.orion.generics.ResponseObject;
import com.orion.service.SignatureService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sign")
public class SignatureController {

    private final SignatureService signatureService;

    public SignatureController(SignatureService signatureService) {
        this.signatureService = signatureService;
    }

    @GetMapping
    public ResponseObject getAgreement(@RequestParam String token) {
        return signatureService.validateAndCompleteRental(token);
    }

//    @PostMapping
//    public ResponseObject signAgreement(@RequestParam String token) {
//        return signatureService.signAgreement(token);
//    }
}