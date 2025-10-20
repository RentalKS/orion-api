package com.orion.controller;

import com.orion.dto.section.SectionDto;
import com.orion.generics.ResponseObject;
import com.orion.security.CustomUserDetails;
import com.orion.service.section.SectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${base.url}/sections")
@RequiredArgsConstructor
@Log4j2
public class SectionController {
    private final SectionService sectionService;

    @Operation(summary = "Create section", description = "Create section")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = SectionDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "404", description = "Files not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @PostMapping
    public ResponseEntity<ResponseObject> createSection(@RequestBody SectionDto sectionDto) {
        String methodName = "createSection";

        log.info("{} -> Create section", methodName);
        ResponseObject response = sectionService.createSection(sectionDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Operation(summary = "Get all section", description = "Get all section")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = SectionDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "404", description = "Files not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @GetMapping("/all")
    public ResponseEntity<ResponseObject> getAllSection(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        String methodName = "getAllSection";

        log.info("{} -> Get all section", methodName);
        ResponseObject response = sectionService.getAllSections(customUserDetails.getUsername());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Operation(summary = "Get section by id", description = "Get section by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = SectionDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "404", description = "Files not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @GetMapping("/{sectionId}")
    public ResponseEntity<ResponseObject> getSection(@PathVariable Long sectionId, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        String methodName = "getSection";

        log.info("{} -> Get section", methodName);
        ResponseObject response = sectionService.getSection(sectionId,customUserDetails.getUsername());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Operation(summary = "Update section", description = "Update section")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = SectionDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "404", description = "Files not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @PutMapping("/update/{sectionId}")
    public ResponseEntity<ResponseObject> updateSection(@PathVariable Long sectionId, @RequestBody SectionDto sectionDto) {
        String methodName = "updateSection";

        log.info("{} -> update section", methodName);
        ResponseObject response = sectionService.updateSection(sectionId, sectionDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Operation(summary = "Delete section", description = "Delete section")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = SectionDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "404", description = "Files not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @PutMapping("/delete/{sectionId}")
    public ResponseEntity<ResponseObject> deleteSection(@PathVariable Long sectionId) {
        String methodName = "deleteSection";

        log.info("{} -> Delete section", methodName);
        ResponseObject response = sectionService.deleteSection(sectionId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
