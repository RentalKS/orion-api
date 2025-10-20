package com.orion.controller;

import com.orion.dto.category.CategoryDto;
import com.orion.generics.ResponseObject;
import com.orion.security.CustomUserDetails;
import com.orion.service.category.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${base.url}/category")
@RequiredArgsConstructor
@Log4j2
public class CategoryController {
    private final CategoryService categoryService;

    @Operation(summary = "Create category", description = "Create category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = CategoryDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "404", description = "Files not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @PreAuthorize("hasAnyRole(@securityService.roleTenant) or hasAnyRole(@securityService.roleAgency)")
    @PostMapping
    public ResponseEntity<ResponseObject> createCategory(@RequestBody CategoryDto companyDto, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        String methodName = "createCategory";

        log.info("{} -> Create category", methodName);
        ResponseObject response = categoryService.createCategory(companyDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Operation(summary = "Get all category", description = "Get all category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = CategoryDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "404", description = "Files not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @PreAuthorize("hasAnyRole(@securityService.roleTenant) or hasAnyRole(@securityService.roleAgency)")
    @GetMapping("/all")
    public ResponseEntity<ResponseObject> getAllCategory(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        String methodName = "getAllCategory";

        log.info("{} -> Get all category", methodName);
        ResponseObject response = categoryService.getAllCategory(customUserDetails.getUsername());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Operation(summary = "Get category by id", description = "Get category by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = CategoryDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "404", description = "Files not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @PreAuthorize("hasAnyRole(@securityService.roleTenant) or hasAnyRole(@securityService.roleAgency)")
    @GetMapping("/{categoryId}")
    public ResponseEntity<ResponseObject> getCategory(@PathVariable Long categoryId,@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        String methodName = "getCategory";

        log.info("{} -> Get category", methodName);
        ResponseObject response = categoryService.getCategory(categoryId,customUserDetails.getUsername());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Operation(summary = "Update category", description = "Update category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = CategoryDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "404", description = "Files not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @PreAuthorize("hasAnyRole(@securityService.roleTenant) or hasAnyRole(@securityService.roleAgency)")
    @PutMapping("/update/{categoryId}")
    public ResponseEntity<ResponseObject> updateCategory(@PathVariable Long categoryId, @RequestBody CategoryDto companyDto) {
        String methodName = "updateCategory";

        log.info("{} -> update category", methodName);
        ResponseObject response = categoryService.updateCategory(categoryId, companyDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Operation(summary = "Delete category", description = "Delete category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = CategoryDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "404", description = "Files not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @PreAuthorize("hasAnyRole(@securityService.roleTenant) or hasAnyRole(@securityService.roleAgency)")
    @PutMapping("/delete/{categoryId}")
    public ResponseEntity<ResponseObject> deleteCategory(@PathVariable Long categoryId) {
        String methodName = "deleteCategory";

        log.info("{} -> Delete category", methodName);
        ResponseObject response = categoryService.deleteCategory(categoryId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
