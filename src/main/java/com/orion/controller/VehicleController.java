package com.orion.controller;

import com.orion.dto.filter.VehicleFilter;
import com.orion.dto.reservation.ReservationDto;
import com.orion.dto.vehicle.VehicleCreateDto;
import com.orion.dto.vehicle.VehicleViewDto;
import com.orion.generics.ResponseObject;
import com.orion.service.vehicle.VehicleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${base.url}/vehicles")
@RequiredArgsConstructor
@Log4j2
public class VehicleController {
    private final VehicleService service;

    @PostMapping
    public ResponseEntity<ResponseObject> createVehicle(@RequestBody VehicleCreateDto vehicleDto) {
        String methodName = "createVehicle";

        log.info("{} -> Create vehicle", methodName);
        ResponseObject response = service.createVehicle(vehicleDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/create-reservation")
    public ResponseEntity<ResponseObject> createReservation(@RequestBody ReservationDto reservationDto) {
        String methodName = "createReservation";

        log.info("{} -> Create reservation", methodName);
        ResponseObject response = service.createReservation(reservationDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
    @GetMapping("/all")
    public ResponseEntity<ResponseObject> getAllVehicles() {
        String methodName = "getAllVehicles";

        log.info("{} -> Get all vehicles", methodName);
        ResponseObject response = service.getAllVehicles();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/{vehicleId}")
    public ResponseEntity<ResponseObject> getVehicle(@PathVariable Long vehicleId) {
        String methodName = "getVehicle";

        log.info("{} -> Get vehicle", methodName);
        ResponseObject response = service.getVehicle(vehicleId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/filter-vehicles")
    public ResponseEntity filterForOrders(@PathVariable("page") Integer page, @PathVariable("size") Integer size,
                                          @RequestBody VehicleFilter filter) {
        String methodName = "filterForOrders";

        log.info("{} -> Filter vehicles", methodName);
        ResponseObject response = service.filterVehicles(page, size, filter);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/update/{vehicleId}")
    public ResponseEntity<ResponseObject> updateVehicle(@PathVariable Long vehicleId, @RequestBody VehicleCreateDto updateDto) {
        String methodName = "updateVehicle";

        log.info("{} -> update vehicle", methodName);
        ResponseObject response = service.updateVehicle(vehicleId, updateDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/delete/{vehicleId}")
    public ResponseEntity<ResponseObject> deleteVehicle(@PathVariable Long vehicleId) {
        String methodName = "deleteVehicle";

        log.info("{} -> Delete vehicle", methodName);
        ResponseObject response = service.deleteVehicle(vehicleId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
