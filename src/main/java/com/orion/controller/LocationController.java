package com.orion.controller;

import com.orion.dto.location.LocationDto;
import com.orion.generics.ResponseObject;
import com.orion.security.CustomUserDetails;
import com.orion.service.location.LocationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${base.url}/locations")
@RequiredArgsConstructor
@Log4j2
public class LocationController {
    private final LocationService locationService;
    @PreAuthorize("hasAnyRole(@securityService.roleAgency)")
    @PostMapping
    public ResponseEntity<ResponseObject> createLocation(@Valid @RequestBody LocationDto locationDto) {
        String methodName = "createLocation";

        log.info("{} -> Create location", methodName);
        ResponseObject response = locationService.createLocation(locationDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/{locationId}")
    public ResponseEntity<ResponseObject> getLocation(@PathVariable Long locationId) {
        String methodName = "getLocation";

        log.info("{} -> Get location", methodName);
        ResponseObject response = locationService.getLocation(locationId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
    @GetMapping
    public ResponseEntity<ResponseObject> getAllLocations(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                          @RequestParam(value = "page",defaultValue = "1") Integer page,
                                                          @RequestParam(value = "size",defaultValue = "10") Integer size) {
        String methodName = "getAll";

        log.info("{} -> Get all location", methodName);
        ResponseObject response = locationService.getAllLocations(customUserDetails.getUsername(), page, size);
        return ResponseEntity.status(response.getStatus()).body(response);
    }


    @PutMapping("/{locationId}")
    public ResponseEntity<ResponseObject> updateLocation(@PathVariable Long locationId, @RequestBody LocationDto locationDto) {
        String methodName = "updateLocation";

        log.info("{} -> update location", methodName);
        ResponseObject response = locationService.updateLocation(locationId, locationDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/delete/{locationId}")
    public ResponseEntity<ResponseObject> deleteLocation(@PathVariable Long locationId) {
        String methodName = "deleteLocation";

        log.info("{} -> Delete location", methodName);
        ResponseObject response = locationService.deleteLocation(locationId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
