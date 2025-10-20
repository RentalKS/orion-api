package com.orion.controller;

import com.orion.dto.dashboard.InfoDashboard;
import com.orion.dto.raportData.ReportData;
import com.orion.dto.raportData.ReportInfo;
import com.orion.security.CustomUserDetails;
import com.orion.service.reports.ExcelGenerationService;
import com.orion.service.reports.PdfGenerationService;
import com.orion.service.reports.ReportingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/reports")
@RequiredArgsConstructor
public class ReportController {
    private final PdfGenerationService pdfGenerationService;
    private final ExcelGenerationService excelGenerationService;
    private final ReportingService reportingService;

    @GetMapping("/vehicle/pdf")
    public ResponseEntity<byte[]> generateVehiclePdfReport() {
        byte[] pdfContent = pdfGenerationService.generateVehicleReport();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "vehicle-report.pdf");
        return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
    }

    @GetMapping("/vehicle/excel")
    public ResponseEntity<byte[]> generateVehicleExcelReport() {
        byte[] excelContent = excelGenerationService.generateVehicleReport();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "vehicle-report.xlsx");
        return new ResponseEntity<>(excelContent, headers, HttpStatus.OK);
    }

    @Operation(summary = "Generate a monthly report in Excel format")
    @PostMapping("/monthly/excel")
    public ResponseEntity<byte[]> generateMonthlyExcelReport(@RequestBody ReportInfo reportInfo, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        byte[] excelContent = reportingService.generateMonthlyReportExcel(reportInfo,customUserDetails.getUsername());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "monthly-report.xlsx");
        return new ResponseEntity<>(excelContent, headers, HttpStatus.OK);
    }

}
