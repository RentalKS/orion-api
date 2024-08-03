package com.orion.controller;

import com.orion.dto.section.SectionDto;
import com.orion.generics.ResponseObject;
import com.orion.service.SectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${base.url}/sections")
@RequiredArgsConstructor
@Log4j2
public class SectionController {
    private final SectionService sectionService;

    @PostMapping
    public ResponseEntity<ResponseObject> createSection(@RequestBody SectionDto sectionDto) {
        String methodName = "createSection";

        log.info("{} -> Create section", methodName);
        ResponseObject response = sectionService.createSection(sectionDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/all")
    public ResponseEntity<ResponseObject> getAllSection() {
        String methodName = "getAllSection";

        log.info("{} -> Get all section", methodName);
        ResponseObject response = sectionService.getAllSections();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/{sectionId}")
    public ResponseEntity<ResponseObject> getSection(@RequestParam Long sectionId) {
        String methodName = "getSection";

        log.info("{} -> Get section", methodName);
        ResponseObject response = sectionService.getSection(sectionId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/update/{sectionId}")
    public ResponseEntity<ResponseObject> updateSection(@RequestParam Long sectionId, @RequestBody SectionDto sectionDto) {
        String methodName = "updateSection";

        log.info("{} -> update section", methodName);
        ResponseObject response = sectionService.updateSection(sectionId, sectionDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/delete/{sectionId}")
    public ResponseEntity<ResponseObject> deleteSection(@RequestParam Long sectionId) {
        String methodName = "deleteSection";

        log.info("{} -> Delete section", methodName);
        ResponseObject response = sectionService.deleteSection(sectionId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
