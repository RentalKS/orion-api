package com.orion.controller;

import com.orion.dto.maintenanceRecord.MaintenanceRecordDto;
import com.orion.generics.ResponseObject;
import com.orion.security.CustomUserDetails;
import com.orion.service.policyVehicle.MaintenanceRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Create maintenance record", description = "Create maintenance record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "404", description = "Files not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @PreAuthorize("hasAnyRole(@securityService.roleAgency)")
    @PostMapping
    public ResponseEntity<ResponseObject> create(@Valid @RequestBody MaintenanceRecordDto maintenanceRecordDto) {
        String methodName = "createMaintenanceRecord";

        log.info("{} -> Create maintenance", methodName);
        ResponseObject response = service.create(maintenanceRecordDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Operation(summary = "Get all maintenance record", description = "Get all maintenance record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = MaintenanceRecordDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "404", description = "Files not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @GetMapping
    public ResponseEntity<ResponseObject> get(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        String methodName = "getMaintenanceRecord";

        log.info("{} -> Get maintenance", methodName);
        ResponseObject response = service.getAll(customUserDetails.getUsername());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Operation(summary = "Get maintenance record by id", description = "Get maintenance record by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = MaintenanceRecordDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "404", description = "Files not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @GetMapping("/{maintenanceRecordId}")
    public ResponseEntity<ResponseObject> getById(@PathVariable Long maintenanceRecordId) {
        String methodName = "getMaintenanceRecord";

        log.info("{} -> Get maintenance", methodName);
        ResponseObject response = service.get(maintenanceRecordId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Operation(summary = "Update maintenance record", description = "Update maintenance record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = MaintenanceRecordDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "404", description = "Files not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @PutMapping("/{maintenanceRecordId}")
    public ResponseEntity<ResponseObject> update(@PathVariable Long maintenanceRecordId, @RequestBody MaintenanceRecordDto maintenanceRecordDto) {
        String methodName = "updateMaintenanceRecord";

        log.info("{} -> Update maintenance", methodName);
        ResponseObject response = service.update(maintenanceRecordId, maintenanceRecordDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Operation(summary = "Delete maintenance record", description = "Delete maintenance record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "404", description = "Files not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @PostMapping("/delete")
    public ResponseEntity<ResponseObject> delete(@Valid @RequestBody Long maintenanceRecordId) {
        String methodName = "deleteMaintenanceRecord";

        log.info("{} -> Delete maintenance", methodName);
        ResponseObject response = service.delete(maintenanceRecordId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

}
