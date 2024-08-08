package com.orion.controller;

import com.orion.dto.location.LocationDto;
import com.orion.generics.ResponseObject;
import com.orion.service.LocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${base.url}/locations")
@RequiredArgsConstructor
@Log4j2
public class LocationController {
    private final LocationService locationService;

    @PostMapping
    public ResponseEntity<ResponseObject> createLocation(@RequestBody LocationDto locationDto) {
        String methodName = "createLocation";

        log.info("{} -> Create location", methodName);
        ResponseObject response = locationService.createLocation(locationDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/{locationId}")
    public ResponseEntity<ResponseObject> getLocation(@RequestParam Long locationId) {
        String methodName = "getLocation";

        log.info("{} -> Get location", methodName);
        ResponseObject response = locationService.getLocation(locationId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/update/{locationId}")
    public ResponseEntity<ResponseObject> updateLocation(@RequestParam Long locationId, @RequestBody LocationDto locationDto) {
        String methodName = "updateLocation";

        log.info("{} -> update location", methodName);
        ResponseObject response = locationService.updateLocation(locationId, locationDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/delete/{locationId}")
    public ResponseEntity<ResponseObject> deleteLocation(@RequestParam Long locationId) {
        String methodName = "deleteLocation";

        log.info("{} -> Delete location", methodName);
        ResponseObject response = locationService.deleteLocation(locationId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
