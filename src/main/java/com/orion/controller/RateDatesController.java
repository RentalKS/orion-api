package com.orion.controller;

import com.orion.dto.rates.RatesDto;
import com.orion.generics.ResponseObject;
import com.orion.service.RateDatesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${base.url}/rates")
@RequiredArgsConstructor
@Log4j2
public class RateDatesController {
    private final RateDatesService rateDatesService;

    @PreAuthorize("hasAnyRole(@securityService.roleTenant) or hasAnyRole(@securityService.roleAgency)")
    @PostMapping
    public ResponseEntity<ResponseObject> createRates(@RequestBody RatesDto ratesDto) {
        String methodName = "createRates";

        log.info("{} -> Create rates", methodName);
        ResponseObject response = rateDatesService.createRateDates(ratesDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PreAuthorize("hasAnyRole(@securityService.roleTenant) or hasAnyRole(@securityService.roleAgency)")
    @GetMapping("/all")
    public ResponseEntity<ResponseObject> getAllRates() {
        String methodName = "getAllRates";

        log.info("{} -> Get all rates", methodName);
        ResponseObject response = rateDatesService.getAllRateDatess();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PreAuthorize("hasAnyRole(@securityService.roleTenant) or hasAnyRole(@securityService.roleAgency)")
    @GetMapping("/{sectionId}")
    public ResponseEntity<ResponseObject> getRates(@RequestParam Long rateDateId) {
        String methodName = "getRates";

        log.info("{} -> Get rates", methodName);
        ResponseObject response = rateDatesService.getRateDates(rateDateId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PreAuthorize("hasAnyRole(@securityService.roleTenant) or hasAnyRole(@securityService.roleAgency)")
    @PutMapping("/update/{rateDateId}")
    public ResponseEntity<ResponseObject> updateRates(@RequestParam Long rateDateId, @RequestBody RatesDto ratesDto) {
        String methodName = "updateRates";

        log.info("{} -> update section", methodName);
        ResponseObject response = rateDatesService.updateRateDates(rateDateId, ratesDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PreAuthorize("hasAnyRole(@securityService.roleTenant) or hasAnyRole(@securityService.roleAgency)")
    @PutMapping("/delete/{rateDateId}")
    public ResponseEntity<ResponseObject> deleteRates(@RequestParam Long rateDateId) {
        String methodName = "deleteRates";

        log.info("{} -> Delete Rates", methodName);
        ResponseObject response = rateDatesService.deleteRateDates(rateDateId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
