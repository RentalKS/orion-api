package com.orion.controller;

import com.orion.generics.ResponseObject;
import com.orion.dto.company.CompanyDto;
import com.orion.security.CustomUserDetails;
import com.orion.security.auth.AuthenticationResponse;
import com.orion.service.company.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${base.url}/companies")
@RequiredArgsConstructor
@Log4j2
public class CompanyController {
    private final CompanyService companyService;

    @Operation(summary = "Create company", description = "Create company")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = CompanyDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "404", description = "Files not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @PreAuthorize("hasAnyRole(@securityService.roleTenant)")
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseObject> createCompany(@Valid @ModelAttribute CompanyDto companyDto) {
        String methodName = "createCompany";

        log.info("{} -> Create company", methodName);
        ResponseObject response = companyService.createCompany(companyDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Operation(summary = "Get company by id", description = "Get company by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = CompanyDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "404", description = "Files not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @PreAuthorize("hasAnyRole(@securityService.roleTenant) or hasAnyRole(@securityService.roleAgency)")
    @GetMapping("/{companyId}")
    public ResponseEntity<ResponseObject> getCompanyById(@PathVariable Long companyId) {
        String methodName = "getCompanyById";

        log.info("{} -> Get my company", methodName);
        ResponseObject response = companyService.getCompanyById(companyId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Operation(summary = "Update company", description = "Update company")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = CompanyDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "404", description = "Files not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @PreAuthorize("hasAnyRole(@securityService.roleTenant)")
    @PutMapping("/{companyId}")
    public ResponseEntity<ResponseObject> updateCompany(@PathVariable Long companyId, @RequestBody CompanyDto companyDto) {
        String methodName = "updateCompany";

        log.info("{} -> update my company", methodName);
        ResponseObject response = companyService.updateCompany(companyId, companyDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Operation(summary = "Delete company", description = "Delete company")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = CompanyDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "404", description = "Files not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @PreAuthorize("hasAnyRole(@securityService.roleTenant) ")
    @DeleteMapping("/{companyId}")
    public ResponseEntity<ResponseObject> deleteCompany(@PathVariable Long companyId) {
        String methodName = "deleteCompany";

        log.info("{} -> Delete my company", methodName);
        ResponseObject response = companyService.deleteCompany(companyId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Operation(summary = "Get my companies", description = "Get my companies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = CompanyDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "404", description = "Files not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @PreAuthorize("hasAnyRole(@securityService.roleAgency)")
    @GetMapping("/my")
    public ResponseEntity<ResponseObject> getMyCompanies(@AuthenticationPrincipal CustomUserDetails principal) {
        String methodName = "getMyCompanies";

        log.info("{} -> Get my companies", methodName);
        ResponseObject response = companyService.getMyCompanies(principal.getUsername());
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
