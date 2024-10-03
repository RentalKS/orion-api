package com.orion.controller;

import com.orion.dto.maintenanceRecord.MaintenanceRecordDto;
import com.orion.generics.ResponseObject;
import com.orion.security.CustomUserDetails;
import com.orion.service.policyVehicle.MaintenanceRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${base.url}/maintenance-records")
@RequiredArgsConstructor
@Log4j2
public class MaintenanceRecordController {
    private final MaintenanceRecordService service;

    @PreAuthorize("hasAnyRole(@securityService.roleAgency)")
    @PostMapping
    public ResponseEntity<ResponseObject> create(@Valid @RequestBody MaintenanceRecordDto maintenanceRecordDto) {
        String methodName = "createMaintenanceRecord";

        log.info("{} -> Create maintenance", methodName);
        ResponseObject response = service.create(maintenanceRecordDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping
    public ResponseEntity<ResponseObject> get(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        String methodName = "getMaintenanceRecord";

        log.info("{} -> Get maintenance", methodName);
        ResponseObject response = service.getAll(customUserDetails.getUsername());
        return ResponseEntity.status(response.getStatus()).body(response);
    }
    @GetMapping("/{maintenanceRecordId}")
    public ResponseEntity<ResponseObject> getAll(@PathVariable Long maintenanceRecordId) {
        String methodName = "getMaintenanceRecord";

        log.info("{} -> Get maintenance", methodName);
        ResponseObject response = service.get(maintenanceRecordId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/{maintenanceRecordId}")
    public ResponseEntity<ResponseObject> update(@PathVariable Long maintenanceRecordId, @RequestBody MaintenanceRecordDto maintenanceRecordDto) {
        String methodName = "updateMaintenanceRecord";

        log.info("{} -> Update maintenance", methodName);
        ResponseObject response = service.update(maintenanceRecordId, maintenanceRecordDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/delete")
    public ResponseEntity<ResponseObject> delete(@Valid @RequestBody Long maintenanceRecordId) {
        String methodName = "deleteMaintenanceRecord";

        log.info("{} -> Delete maintenance", methodName);
        ResponseObject response = service.delete(maintenanceRecordId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

}
