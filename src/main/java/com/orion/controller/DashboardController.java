package com.orion.controller;

import com.orion.dto.dashboard.InfoDashboard;
import com.orion.generics.ResponseObject;
import com.orion.security.CustomUserDetails;
import com.orion.service.dashboard.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${base.url}/dashboard")
@RequiredArgsConstructor
@Log4j2
public class DashboardController {
    private final DashboardService dashboardService;

    @Operation(summary = "Get dashboard data", description = "Get dashboard data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = InfoDashboard.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "404", description = "Files not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @PostMapping
    public ResponseEntity<ResponseObject> dashboard(@RequestBody InfoDashboard infoDashboard, @AuthenticationPrincipal CustomUserDetails userDetails) {
        String methodName = "dashboard";

        log.info("{} -> Process dashboard", methodName);
        ResponseObject response = dashboardService.getDashboardRentalData(userDetails, infoDashboard);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Operation(summary = "Get vehicle dashboard data", description = "Get vehicle dashboard data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = InfoDashboard.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "404", description = "Files not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @PostMapping("/vehicles")
    public ResponseEntity<ResponseObject> vehicleDashboard(@RequestBody InfoDashboard infoDashboard, @AuthenticationPrincipal CustomUserDetails userDetails) {
        String methodName = "dashboard";

        log.info("{} -> Process dashboard", methodName);
        ResponseObject response = dashboardService.getVehiclesDashboardData(userDetails, infoDashboard);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
