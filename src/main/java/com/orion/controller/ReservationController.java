package com.orion.controller;

import com.orion.dto.filter.VehicleFilter;
import com.orion.dto.reservation.ReservationDto;
import com.orion.dto.vehicle.VehicleDto;
import com.orion.generics.ResponseObject;
import com.orion.security.CustomUserDetails;
import com.orion.service.ReservationService;
import com.orion.service.vehicle.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${base.url}/reservation")
@RequiredArgsConstructor
@Log4j2
public class ReservationController {
    private final ReservationService service;
    @PostMapping
    public ResponseEntity<ResponseObject> createReservation(@RequestBody ReservationDto reservationDto) {
        String methodName = "createReservation";

        log.info("{} -> Create reservation", methodName);
        ResponseObject response = service.create(reservationDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
