package com.orion.controller;

import com.orion.dto.filter.VehicleFilter;
import com.orion.dto.reservation.ReservationDto;
import com.orion.dto.vehicle.VehicleDto;
import com.orion.generics.ResponseObject;
import com.orion.security.CustomUserDetails;
import com.orion.service.vehicle.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${base.url}/vehicle")
@RequiredArgsConstructor
@Log4j2
public class VehicleController {
    private final VehicleService service;

    @PostMapping
    public ResponseEntity<ResponseObject> createVehicle(@Valid @RequestBody VehicleDto vehicleDto, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        String methodName = "createVehicle";

        log.info("{} -> Create vehicle", methodName);
        ResponseObject response = service.create(vehicleDto, customUserDetails.getUsername());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/all")
    public ResponseEntity<ResponseObject> getAllVehicles(@RequestBody VehicleFilter filter, @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                         @RequestParam(value = "page",defaultValue = "1") Integer page,
                                                         @RequestParam(value = "size",defaultValue = "10") Integer size, @RequestParam("search") String search) {
        String methodName = "getAllVehicles";

        log.info("{} -> Get all vehicles", methodName);
        ResponseObject response = service.getAll(filter,customUserDetails.getUsername(),page,size,search);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/{vehicleId}")
    public ResponseEntity<ResponseObject> getVehicle(@PathVariable Long vehicleId) {
        String methodName = "getVehicle";

        log.info("{} -> Get vehicle", methodName);
        ResponseObject response = service.get(vehicleId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/update/{vehicleId}")
    public ResponseEntity<ResponseObject> updateVehicle(@PathVariable Long vehicleId, @RequestBody VehicleDto updateDto) {
        String methodName = "updateVehicle";

        log.info("{} -> update vehicle", methodName);
        ResponseObject response = service.update(vehicleId, updateDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/delete/{vehicleId}")
    public ResponseEntity<ResponseObject> deleteVehicle(@PathVariable Long vehicleId) {
        String methodName = "deleteVehicle";

        log.info("{} -> Delete vehicle", methodName);
        ResponseObject response = service.delete(vehicleId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
