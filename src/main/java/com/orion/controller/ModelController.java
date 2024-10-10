package com.orion.controller;

import com.orion.dto.model.ModelDto;
import com.orion.generics.ResponseObject;
import com.orion.security.CustomUserDetails;
import com.orion.service.model.ModelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${base.url}/models")
@RequiredArgsConstructor
@Log4j2
public class ModelController {
    private final ModelService modelService;

    @PreAuthorize("hasAnyRole(@securityService.roleTenant) or hasAnyRole(@securityService.roleAgency)")
    @PostMapping
    public ResponseEntity<ResponseObject> createModel(@Valid @RequestBody ModelDto modelDto) {
        String methodName = "createModel";

        log.info("{} -> Create model", methodName);
        ResponseObject response = modelService.createModel(modelDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PreAuthorize("hasAnyRole(@securityService.roleTenant) or hasAnyRole(@securityService.roleAgency)")
    @GetMapping("/all")
    public ResponseEntity<ResponseObject> getAllModel(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                      @RequestParam(defaultValue = "1") int page,
                                                      @RequestParam(defaultValue = "10") int size) {
        String methodName = "getAllModel";

        log.info("{} -> Get all model", methodName);
        ResponseObject response = modelService.getAllModels(customUserDetails.getUsername(), page, size);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PreAuthorize("hasAnyRole(@securityService.roleTenant) or hasAnyRole(@securityService.roleAgency)")
    @GetMapping("/{modelId}")
    public ResponseEntity<ResponseObject> getModel(@PathVariable Long modelId) {
        String methodName = "getModel";

        log.info("{} -> Get model", methodName);
        ResponseObject response = modelService.getModel(modelId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PreAuthorize("hasAnyRole(@securityService.roleTenant) or hasAnyRole(@securityService.roleAgency)")
    @PutMapping("/update/{modelId}")
    public ResponseEntity<ResponseObject> updateModel(@PathVariable Long modelId, @RequestBody ModelDto modelDto) {
        String methodName = "updateModel";

        log.info("{} -> update model", methodName);
        ResponseObject response = modelService.updateModel(modelId, modelDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PreAuthorize("hasAnyRole(@securityService.roleTenant) or hasAnyRole(@securityService.roleAgency)")
    @PutMapping("/delete/{modelId}")
    public ResponseEntity<ResponseObject> deleteModel(@PathVariable Long modelId) {
        String methodName = "deleteModel";

        log.info("{} -> Delete model", methodName);
        ResponseObject response = modelService.deleteModel(modelId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
