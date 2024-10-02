package com.orion.controller;

import com.orion.dto.dashboard.InfoDashboard;
import com.orion.generics.ResponseObject;
import com.orion.security.CustomUserDetails;
import com.orion.service.dashboard.DashboardService;
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

    @PostMapping
    public ResponseEntity<ResponseObject> dashboard(@RequestBody InfoDashboard infoDashboard, @AuthenticationPrincipal CustomUserDetails userDetails) {
        String methodName = "dashboard";

        log.info("{} -> Process dashboard", methodName);
        ResponseObject response = dashboardService.getDashboardData(userDetails, infoDashboard);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/vehicles")
    public ResponseEntity<ResponseObject> vehicleDashboard(@RequestBody InfoDashboard infoDashboard, @AuthenticationPrincipal CustomUserDetails userDetails) {
        String methodName = "dashboard";

        log.info("{} -> Process dashboard", methodName);
        ResponseObject response = dashboardService.getVehiclesDashboardData(userDetails, infoDashboard);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
