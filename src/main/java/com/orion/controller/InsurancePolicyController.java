package com.orion.controller;

import com.orion.dto.insurancePolicy.InsurancePolicyDto;
import com.orion.generics.ResponseObject;
import com.orion.security.CustomUserDetails;
import com.orion.service.policyVehicle.InsurancePolicyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${base.url}/insurance-policies")
@RequiredArgsConstructor
@Log4j2
public class InsurancePolicyController {
    private final InsurancePolicyService service;

    @PostMapping
    public ResponseEntity<ResponseObject> create(@Valid @RequestBody InsurancePolicyDto insurancePolicyDto) {
        String methodName = "createInsurancePolicy";
        log.info("{} -> Create insurance policy", methodName);
        ResponseObject response = service.create(insurancePolicyDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/{insurancePolicyId}")
    public ResponseEntity<ResponseObject> getById(@PathVariable Long insurancePolicyId) {
        String methodName = "getInsurancePolicy";
        log.info("{} -> Get insurance policy", methodName);
        ResponseObject response = service.get(insurancePolicyId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
    @GetMapping
    public ResponseEntity<ResponseObject> getAll(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        String methodName = "getInsurancePolicy";
        log.info("{} -> Get insurance policy", methodName);
        ResponseObject response = service.getAll(customUserDetails.getUsername());
        return ResponseEntity.status(response.getStatus()).body(response);
    }
    @PutMapping("/{insurancePolicyId}")
    public ResponseEntity<ResponseObject> update(@PathVariable Long insurancePolicyId, @RequestBody InsurancePolicyDto insurancePolicyDto) {
        String methodName = "updateInsurancePolicy";
        log.info("{} -> Update insurance policy", methodName);
        ResponseObject response = service.updateInsurancePolicy(insurancePolicyId, insurancePolicyDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
    @PutMapping("/delete/{insurancePolicyId}")
    public ResponseEntity<ResponseObject> delete(@PathVariable Long insurancePolicyId) {
        String methodName = "deleteInsurancePolicy";
        log.info("{} -> Delete insurance policy", methodName);
        ResponseObject response = service.delete(insurancePolicyId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
