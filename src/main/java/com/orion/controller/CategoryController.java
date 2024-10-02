package com.orion.controller;

import com.orion.dto.category.CategoryDto;
import com.orion.generics.ResponseObject;
import com.orion.security.CustomUserDetails;
import com.orion.service.category.CategoryService;
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

    @PreAuthorize("hasAnyRole(@securityService.roleTenant) or hasAnyRole(@securityService.roleAgency)")
    @PostMapping
    public ResponseEntity<ResponseObject> createCategory(@RequestBody CategoryDto companyDto, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        String methodName = "createCategory";

        log.info("{} -> Create category", methodName);
        ResponseObject response = categoryService.createCategory(companyDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PreAuthorize("hasAnyRole(@securityService.roleTenant) or hasAnyRole(@securityService.roleAgency)")
    @GetMapping("/all")
    public ResponseEntity<ResponseObject> getAllCategory(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        String methodName = "getAllCategory";

        log.info("{} -> Get all category", methodName);
        ResponseObject response = categoryService.getAllCategory(customUserDetails.getUsername());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PreAuthorize("hasAnyRole(@securityService.roleTenant) or hasAnyRole(@securityService.roleAgency)")
    @GetMapping("/{categoryId}")
    public ResponseEntity<ResponseObject> getCategory(@RequestParam Long categoryId,@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        String methodName = "getCategory";

        log.info("{} -> Get category", methodName);
        ResponseObject response = categoryService.getCategory(categoryId,customUserDetails.getUsername());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PreAuthorize("hasAnyRole(@securityService.roleTenant) or hasAnyRole(@securityService.roleAgency)")
    @PutMapping("/update/{categoryId}")
    public ResponseEntity<ResponseObject> updateCategory(@RequestParam Long categoryId, @RequestBody CategoryDto companyDto) {
        String methodName = "updateCategory";

        log.info("{} -> update category", methodName);
        ResponseObject response = categoryService.updateCategory(categoryId, companyDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PreAuthorize("hasAnyRole(@securityService.roleTenant) or hasAnyRole(@securityService.roleAgency)")
    @PutMapping("/delete/{categoryId}")
    public ResponseEntity<ResponseObject> deleteCategory(@RequestParam Long categoryId) {
        String methodName = "deleteCategory";

        log.info("{} -> Delete category", methodName);
        ResponseObject response = categoryService.deleteCategory(categoryId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
