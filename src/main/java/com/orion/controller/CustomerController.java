package com.orion.controller;

import com.orion.dto.customer.CustomerDto;
import com.orion.generics.ResponseObject;
import com.orion.service.customer.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${base.url}/customer")
@RequiredArgsConstructor
@Log4j2
public class CustomerController {
    private final CustomerService customerService;

    @PreAuthorize("hasAnyRole(@securityService.roleTenant) or hasAnyRole(@securityService.roleAgency)")
    @PostMapping
    public ResponseEntity<ResponseObject> createCustomer(@RequestBody CustomerDto customerDto) {
        String methodName = "createCustomer";

        log.info("{} -> Create customer", methodName);
        ResponseObject response = customerService.createCustomer(customerDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PreAuthorize("hasAnyRole(@securityService.roleTenant) or hasAnyRole(@securityService.roleAgency)")
    @GetMapping("/{customerId}")
    public ResponseEntity<ResponseObject> getCustomer(@RequestParam Long customerId) {
        String methodName = "getCustomer";

        log.info("{} -> Get customer", methodName);
        ResponseObject response = customerService.getCustomer(customerId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PreAuthorize("hasAnyRole(@securityService.roleTenant) or hasAnyRole(@securityService.roleAgency)")
    @PutMapping("/update/{customerId}")
    public ResponseEntity<ResponseObject> updateCustomer(@RequestParam Long customerId, @RequestBody CustomerDto customerDto) {
        String methodName = "updateCustomer";

        log.info("{} -> update customer", methodName);
        ResponseObject response = customerService.updateCustomer(customerId, customerDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PreAuthorize("hasAnyRole(@securityService.roleTenant) or hasAnyRole(@securityService.roleAgency)")
    @PutMapping("/delete/{customerId}")
    public ResponseEntity<ResponseObject> deleteCustomer(@RequestParam Long customerId) {
        String methodName = "deleteCustomer";

        log.info("{} -> Delete customer", methodName);
        ResponseObject response = customerService.deleteCustomer(customerId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
