package com.orion.controller;

import com.orion.dto.brand.BrandDto;
import com.orion.generics.ResponseObject;
import com.orion.security.CustomUserDetails;
import com.orion.service.brand.BrandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${base.url}/brand")
@RequiredArgsConstructor
@Log4j2
public class BrandController {
    private final BrandService brandService;
    @PreAuthorize("hasAnyRole(@securityService.roleAgency)")
    @PostMapping
    public ResponseEntity<ResponseObject> create(@Valid @RequestBody BrandDto brandDto) {
        String methodName = "create";

        log.info("{} -> Create brand", methodName);
        ResponseObject response = brandService.create(brandDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/{brandId}")
    public ResponseEntity<ResponseObject> get(@PathVariable Long brandId,@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        String methodName = "getBrand";

        log.info("{} -> Get brand", methodName);
        ResponseObject response = brandService.get(brandId,customUserDetails.getUsername());
        return ResponseEntity.status(response.getStatus()).body(response);
    }
    @GetMapping
    public ResponseEntity<ResponseObject> getAll(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        String methodName = "getAll";

        log.info("{} -> Get all brand", methodName);
        ResponseObject response = brandService.getAll(customUserDetails.getUsername());
        return ResponseEntity.status(response.getStatus()).body(response);
    }
    @PutMapping("/update/{brandId}")
    public ResponseEntity<ResponseObject> update(@PathVariable Long brandId, @RequestBody BrandDto brandDto) {
        String methodName = "update";

        log.info("{} -> update brand", methodName);
        ResponseObject response = brandService.update(brandId, brandDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
    @PutMapping("/delete/{brandId}")
    public ResponseEntity<ResponseObject> delete(@PathVariable Long brandId) {
        String methodName = "deleteLocation";

        log.info("{} -> Delete brand", methodName);
        ResponseObject response = brandService.delete(brandId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
