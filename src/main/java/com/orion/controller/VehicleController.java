package com.orion.controller;

import com.orion.dto.filter.VehicleFilter;
import com.orion.dto.reservation.ReservationDto;
import com.orion.dto.vehicle.VehicleDto;
import com.orion.generics.ResponseObject;
import com.orion.service.VehicleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${base.url}/vehicles")
@RequiredArgsConstructor
@Log4j2
public class VehicleController {
    private final VehicleService vehicleService;

    @PostMapping
    public ResponseEntity<ResponseObject> createVehicle(@RequestBody VehicleDto vehicleDto) {
        String methodName = "createVehicle";

        log.info("{} -> Create vehicle", methodName);
        ResponseObject response = vehicleService.createVehicle(vehicleDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/create-reservation")
    public ResponseEntity<ResponseObject> createReservation(@RequestBody ReservationDto reservationDto) {
        String methodName = "createReservation";

        log.info("{} -> Create reservation", methodName);
        ResponseObject response = vehicleService.createReservation(reservationDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
    @GetMapping("/all")
    public ResponseEntity<ResponseObject> getAllVehicles() {
        String methodName = "getAllVehicles";

        log.info("{} -> Get all vehicles", methodName);
        ResponseObject response = vehicleService.getAllVehicles();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/{vehicleId}")
    public ResponseEntity<ResponseObject> getVehicle(@RequestParam Long vehicleId) {
        String methodName = "getVehicle";

        log.info("{} -> Get vehicle", methodName);
        ResponseObject response = vehicleService.getVehicle(vehicleId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/filter-vehicles")
    public ResponseEntity filterForOrders(@RequestParam("page") Integer page, @RequestParam("size") Integer size,
                                          @RequestBody VehicleFilter filter, @AuthenticationPrincipal UserDetails userDetails) {
        String methodName = "filterForOrders";

        log.info("{} -> Filter vehicles", methodName);
        ResponseObject response = vehicleService.filterVehicles(page, size, filter,userDetails);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/update/{vehicleId}")
    public ResponseEntity<ResponseObject> updateVehicle(@RequestParam Long vehicleId, @RequestBody VehicleDto vehicleDto) {
        String methodName = "updateVehicle";

        log.info("{} -> update vehicle", methodName);
        ResponseObject response = vehicleService.updateVehicle(vehicleId, vehicleDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/delete/{vehicleId}")
    public ResponseEntity<ResponseObject> deleteVehicle(@RequestParam Long vehicleId) {
        String methodName = "deleteVehicle";

        log.info("{} -> Delete vehicle", methodName);
        ResponseObject response = vehicleService.deleteVehicle(vehicleId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
