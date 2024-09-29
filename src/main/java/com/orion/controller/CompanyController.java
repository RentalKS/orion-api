package com.orion.controller;

import com.orion.generics.ResponseObject;
import com.orion.dto.company.CompanyDto;
import com.orion.security.CustomUserDetails;
import com.orion.service.CompanyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${base.url}/companies")
@RequiredArgsConstructor
@Log4j2
public class CompanyController {
    private final CompanyService companyService;

    @PreAuthorize("hasAnyRole(@securityService.roleTenant)")
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseObject> createCompany(@ModelAttribute CompanyDto companyDto) {
        String methodName = "createCompany";

        log.info("{} -> Create company", methodName);
        ResponseObject response = companyService.createCompany(companyDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

//    @PreAuthorize("hasAnyRole(@securityService.roleTenant) or hasAnyRole(@securityService.roleAgency)")
    @GetMapping("/{companyId}")
    public ResponseEntity<ResponseObject> getCompanyById(@PathVariable Long companyId, @AuthenticationPrincipal CustomUserDetails principal) {
        String methodName = "getCompanyById";

        log.info("{} -> Get my company", methodName);
        ResponseObject response = companyService.getCompanyById(companyId, principal.getUsername());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PreAuthorize("hasAnyRole(@securityService.roleTenant)")
    @PutMapping("/{companyId}")
    public ResponseEntity<ResponseObject> updateCompany(@PathVariable Long companyId, @RequestBody CompanyDto companyDto) {
        String methodName = "updateCompany";

        log.info("{} -> update my company", methodName);
        ResponseObject response = companyService.updateCompany(companyId, companyDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PreAuthorize("hasAnyRole(@securityService.roleTenant) ")
    @DeleteMapping("/{companyId}")
    public ResponseEntity<ResponseObject> deleteCompany(@PathVariable Long companyId) {
        String methodName = "deleteCompany";

        log.info("{} -> Delete my company", methodName);
        ResponseObject response = companyService.deleteCompany(companyId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PreAuthorize("hasAnyRole(@securityService.roleTenant) or hasAnyRole(@securityService.roleAgency)")
    @GetMapping("/my")
    public ResponseEntity<ResponseObject> getMyCompanies(@AuthenticationPrincipal CustomUserDetails principal) {
        String methodName = "getMyCompanies";

        log.info("{} -> Get my companies", methodName);
        ResponseObject response = companyService.getMyCompanies(principal.getUsername());
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
