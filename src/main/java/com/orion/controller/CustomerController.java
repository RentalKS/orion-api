package com.orion.controller;

import com.orion.dto.customer.CustomerDto;
import com.orion.generics.ResponseObject;
import com.orion.security.CustomUserDetails;
import com.orion.service.customer.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("${base.url}/customer")
@RequiredArgsConstructor
@Log4j2
public class CustomerController {
    private final CustomerService customerService;

    @Operation(summary = "Create customer", description = "Create customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "404", description = "Files not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @PreAuthorize("hasAnyRole(@securityService.roleTenant) or hasAnyRole(@securityService.roleAgency)")
    @PostMapping
    public ResponseEntity<ResponseObject> createCustomer(@Valid @RequestBody CustomerDto customerDto) {
        String methodName = "createCustomer";

        log.info("{} -> Create customer", methodName);
        ResponseObject response = customerService.createCustomer(customerDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Operation(summary = "Get customer by id", description = "Get customer by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "404", description = "Files not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @PreAuthorize("hasAnyRole(@securityService.roleTenant) or hasAnyRole(@securityService.roleAgency)")
    @GetMapping("/{customerId}")
    public ResponseEntity<ResponseObject> getCustomer(@PathVariable Long customerId) {
        String methodName = "getCustomer";

        log.info("{} -> Get customer", methodName);
        ResponseObject response = customerService.getCustomer(customerId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Operation(summary = "Get all customer", description = "Get all page size and search customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "404", description = "Files not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @PreAuthorize("hasAnyRole(@securityService.roleTenant) or hasAnyRole(@securityService.roleAgency)")
    @GetMapping
    public ResponseEntity<ResponseObject> getAll(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                 @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                 @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                 @RequestParam(value = "search", required = false) String search) {
        String methodName = "getAllCustomer";

        log.info("{} -> Get all customer", methodName);
        ResponseObject response = customerService.getAll(customUserDetails.getUsername(), page, size, search);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Operation(summary = "Update customer", description = "Update customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "404", description = "Files not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @PreAuthorize("hasAnyRole(@securityService.roleTenant) or hasAnyRole(@securityService.roleAgency)")
    @PutMapping("/update/{customerId}")
    public ResponseEntity<ResponseObject> updateCustomer(@PathVariable Long customerId, @RequestBody CustomerDto customerDto) {
        String methodName = "updateCustomer";

        log.info("{} -> update customer", methodName);
        ResponseObject response = customerService.updateCustomer(customerId, customerDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Operation(summary = "Delete customer", description = "Delete customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "404", description = "Files not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @PreAuthorize("hasAnyRole(@securityService.roleTenant) or hasAnyRole(@securityService.roleAgency)")
    @PutMapping("/delete/{customerId}")
    public ResponseEntity<ResponseObject> deleteCustomer(@PathVariable Long customerId) {
        String methodName = "deleteCustomer";

        log.info("{} -> Delete customer", methodName);
        ResponseObject response = customerService.deleteCustomer(customerId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
