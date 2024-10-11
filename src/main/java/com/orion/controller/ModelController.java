package com.orion.controller;

import com.orion.dto.model.ModelDto;
import com.orion.generics.ResponseObject;
import com.orion.security.CustomUserDetails;
import com.orion.service.model.ModelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
@RequestMapping("${base.url}/models")
@RequiredArgsConstructor
@Log4j2
public class ModelController {
    private final ModelService modelService;

    @Operation(summary = "Create model", description = "Create model")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ModelDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "404", description = "Files not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @PreAuthorize("hasAnyRole(@securityService.roleTenant) or hasAnyRole(@securityService.roleAgency)")
    @PostMapping
    public ResponseEntity<ResponseObject> createModel(@Valid @RequestBody ModelDto modelDto) {
        String methodName = "createModel";

        log.info("{} -> Create model", methodName);
        ResponseObject response = modelService.createModel(modelDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Operation(summary = "Get all model", description = "Get all model")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ModelDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "404", description = "Files not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
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

    @Operation(summary = "Get model by id", description = "Get model by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ModelDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "404", description = "Files not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @PreAuthorize("hasAnyRole(@securityService.roleTenant) or hasAnyRole(@securityService.roleAgency)")
    @GetMapping("/{modelId}")
    public ResponseEntity<ResponseObject> getModel(@PathVariable Long modelId) {
        String methodName = "getModel";

        log.info("{} -> Get model", methodName);
        ResponseObject response = modelService.getModel(modelId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Operation(summary = "Update model", description = "Update model")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ModelDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "404", description = "Files not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @PreAuthorize("hasAnyRole(@securityService.roleTenant) or hasAnyRole(@securityService.roleAgency)")
    @PutMapping("/update/{modelId}")
    public ResponseEntity<ResponseObject> updateModel(@PathVariable Long modelId, @RequestBody ModelDto modelDto) {
        String methodName = "updateModel";

        log.info("{} -> update model", methodName);
        ResponseObject response = modelService.updateModel(modelId, modelDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Operation(summary = "Delete model", description = "Delete model")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ModelDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "404", description = "Files not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @PreAuthorize("hasAnyRole(@securityService.roleTenant) or hasAnyRole(@securityService.roleAgency)")
    @PutMapping("/delete/{modelId}")
    public ResponseEntity<ResponseObject> deleteModel(@PathVariable Long modelId) {
        String methodName = "deleteModel";

        log.info("{} -> Delete model", methodName);
        ResponseObject response = modelService.deleteModel(modelId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
