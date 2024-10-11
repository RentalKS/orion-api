package com.orion.controller;

import com.orion.dto.insurancePolicy.InsurancePolicyDto;
import com.orion.generics.ResponseObject;
import com.orion.security.CustomUserDetails;
import com.orion.service.policyVehicle.InsurancePolicyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Create insurance policy", description = "Create insurance policy")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = InsurancePolicyDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "404", description = "Files not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @PostMapping
    public ResponseEntity<ResponseObject> create(@Valid @RequestBody InsurancePolicyDto insurancePolicyDto) {
        String methodName = "createInsurancePolicy";
        log.info("{} -> Create insurance policy", methodName);
        ResponseObject response = service.create(insurancePolicyDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Operation(summary = "Get insurance policy by id", description = "Get insurance policy by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = InsurancePolicyDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "404", description = "Files not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @GetMapping("/{insurancePolicyId}")
    public ResponseEntity<ResponseObject> getById(@PathVariable Long insurancePolicyId) {
        String methodName = "getInsurancePolicy";
        log.info("{} -> Get insurance policy", methodName);
        ResponseObject response = service.get(insurancePolicyId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Operation(summary = "Get all insurance policy", description = "Get all insurance policy")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = InsurancePolicyDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "404", description = "Files not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @GetMapping
    public ResponseEntity<ResponseObject> getAll(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        String methodName = "getInsurancePolicy";
        log.info("{} -> Get insurance policy", methodName);
        ResponseObject response = service.getAll(customUserDetails.getUsername());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Operation(summary = "Update insurance policy", description = "Update insurance policy")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = InsurancePolicyDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "404", description = "Files not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @PutMapping("/{insurancePolicyId}")
    public ResponseEntity<ResponseObject> update(@PathVariable Long insurancePolicyId, @RequestBody InsurancePolicyDto insurancePolicyDto) {
        String methodName = "updateInsurancePolicy";
        log.info("{} -> Update insurance policy", methodName);
        ResponseObject response = service.updateInsurancePolicy(insurancePolicyId, insurancePolicyDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Operation(summary = "Delete insurance policy", description = "Delete insurance policy")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = InsurancePolicyDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "404", description = "Files not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @PutMapping("/delete/{insurancePolicyId}")
    public ResponseEntity<ResponseObject> delete(@PathVariable Long insurancePolicyId) {
        String methodName = "deleteInsurancePolicy";
        log.info("{} -> Delete insurance policy", methodName);
        ResponseObject response = service.delete(insurancePolicyId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
