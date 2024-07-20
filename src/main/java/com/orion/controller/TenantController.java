package com.orion.controller;

import com.orion.common.ResponseObject;
import com.orion.dto.TenantDto;
import com.orion.dto.tenant.UpdateTenantDto;
import com.orion.service.TenantService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/${base.url}/tenant")
@RequiredArgsConstructor
@Log4j2
public class TenantController {
    private TenantService tenantService;

    @PreAuthorize("hasAuthority(@securityService.roleAdmin)")
    @PostMapping("/create")
    public ResponseEntity createTenant(@RequestBody @Valid TenantDto createTenant) {
        String methodName = "createTenant";

        log.info("{} -> Create Tenant", methodName);
        ResponseObject responseObject = tenantService.create(createTenant);

        log.info("{} -> Create Tenant, response status: {}",methodName, responseObject.getCode());
        return ResponseEntity.status(responseObject.getStatus()).body(responseObject);
    }

    @Operation(summary = "Get Tenant by Id", description = "")
    @GetMapping("/{tenantId}")
    public ResponseEntity getTenantById(@PathVariable("tenantId") Long tenantId) {
        String methodName = "getTenant";

        log.info("{} -> Get Tenant by Id", methodName);
        ResponseObject responseObject = tenantService.getTenantById(tenantId);

        log.info("{} -> Get Tenant by Id, response status: {}",methodName, responseObject.getCode());
        return ResponseEntity.status(responseObject.getStatus()).body(responseObject);
    }

    @PreAuthorize("hasAuthority(@securityService.roleAdmin)")
    @GetMapping("/all")
    public ResponseEntity getAllTenants(@RequestParam("page") Integer page, @RequestParam("size") Integer size, @RequestParam(required = false, name = "search") String search) {
        String methodName = "getAllTenants";

        log.info("{} -> Get all Tenants", methodName);
        ResponseObject responseObject = tenantService.getAll(page, size, search);

        log.info("{} -> Get all Tenants, response status: {}",methodName, responseObject.getCode());
        return ResponseEntity.status(responseObject.getStatus()).body(responseObject);
    }

    @PreAuthorize("hasAuthority(@securityService.roleAdmin)")
    @PutMapping("/update")
    public ResponseEntity updateTenant(@RequestBody UpdateTenantDto updateTenant) {
        String methodName = "updateTenant";

        log.info("{} -> Update Tenant by id", methodName);
        ResponseObject responseObject = tenantService.updateTenant(updateTenant);

        log.info("{} -> Update Tenant by id, response status: {}",methodName, responseObject.getCode());
        return ResponseEntity.status(responseObject.getStatus()).body(responseObject);
    }

    @PreAuthorize("hasAuthority(@securityService.roleAdmin)")
    @PutMapping("/delete/{tenantId}")
    public ResponseEntity deleteTenant(@PathVariable("tenantId") Long tenantId) {
        String methodName = "deleteTenant";

        log.info("{} -> Delete Tenant by Id", methodName);
        ResponseObject responseObject = tenantService.deleteTenant(tenantId);

        log.info("{} -> Delete Tenant by Id, response status: {}",methodName, responseObject.getCode());
        return ResponseEntity.status(responseObject.getStatus()).body(responseObject);
    }

}
