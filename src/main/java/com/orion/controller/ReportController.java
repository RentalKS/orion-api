package com.orion.controller;

import com.orion.service.reports.ExcelGenerationService;
import com.orion.service.reports.PdfGenerationService;
import com.orion.service.reports.ReportingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/monthly/excel")
    public ResponseEntity<byte[]> generateMonthlyExcelReport() {
        byte[] excelContent = reportingService.generateMonthlyReportExcel();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "monthly-report.xlsx");
        return new ResponseEntity<>(excelContent, headers, HttpStatus.OK);
    }

}
