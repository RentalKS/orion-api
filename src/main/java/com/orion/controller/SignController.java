package com.orion.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sign")
public class SignController {

    @GetMapping
    public ResponseEntity<String> signAgreement(@RequestParam("token") String token) {
        // Process the token, validate it, or return a success message
        return ResponseEntity.ok("Token received: " + token);
    }
}
