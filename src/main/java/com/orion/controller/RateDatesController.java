package com.orion.controller;

import com.orion.dto.rates.RatesDto;
import com.orion.generics.ResponseObject;
import com.orion.security.CustomUserDetails;
import com.orion.service.rental.RateDatesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Path;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${base.url}/rate-dates")
@RequiredArgsConstructor
@Log4j2
public class RateDatesController {
    private final RateDatesService service;

    @Operation(summary = "Create rates", description = "Create rates")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = RatesDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "404", description = "Files not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @PreAuthorize("hasAnyRole(@securityService.roleTenant) or hasAnyRole(@securityService.roleAgency)")
    @PostMapping
    public ResponseEntity<ResponseObject> createRates(@Valid @RequestBody RatesDto ratesDto) {
        String methodName = "createRates";

        log.info("{} -> Create rates", methodName);
        ResponseObject response = service.createRateDates(ratesDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
    
    @Operation(summary = "Get all rates", description = "Get all rates")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = RatesDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "404", description = "Files not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @PreAuthorize("hasAnyRole(@securityService.roleTenant) or hasAnyRole(@securityService.roleAgency)")
    @GetMapping
    public ResponseEntity<ResponseObject> getAllRates(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        String methodName = "getAllRates";

        log.info("{} -> Get all rates", methodName);
        ResponseObject response = service.getAllRateDates(customUserDetails.getUsername());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Operation(summary = "Get rates", description = "Get rates")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = RatesDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "404", description = "Files not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @PreAuthorize("hasAnyRole(@securityService.roleTenant) or hasAnyRole(@securityService.roleAgency)")
    @GetMapping("/{rateId}")
    public ResponseEntity<ResponseObject> getRates(@PathVariable Long rateId,@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        String methodName = "getRates";

        log.info("{} -> Get rates", methodName);
        ResponseObject response = service.getRateDates(rateId,customUserDetails.getUsername());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Operation(summary = "Update rates", description = "Update rates")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = RatesDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "404", description = "Files not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @PreAuthorize("hasAnyRole(@securityService.roleTenant) or hasAnyRole(@securityService.roleAgency)")
    @PutMapping("/update/{rateDateId}")
    public ResponseEntity<ResponseObject> updateRates(@PathVariable Long rateDateId, @RequestBody RatesDto ratesDto) {
        String methodName = "updateRates";

        log.info("{} -> update section", methodName);
        ResponseObject response = service.updateRateDates(rateDateId, ratesDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Operation(summary = "Delete rates", description = "Delete rates")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = RatesDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "404", description = "Files not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @PreAuthorize("hasAnyRole(@securityService.roleTenant) or hasAnyRole(@securityService.roleAgency)")
    @PutMapping("/delete/{rateDateId}")
    public ResponseEntity<ResponseObject> deleteRates(@PathVariable Long rateDateId) {
        String methodName = "deleteRates";

        log.info("{} -> Delete Rates", methodName);
        ResponseObject response = service.deleteRateDates(rateDateId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
