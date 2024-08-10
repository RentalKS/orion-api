package com.orion.service;

import com.orion.entity.Vehicle;
import com.orion.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class ExcelGenerationService {

    private final VehicleRepository vehicleRepository;

    public byte[] generateVehicleReport() {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Vehicles");

            Row headerRow = sheet.createRow(0);
            String[] columns = {"ID", "Registration Number", "Model", "Color", "Year", "Fuel Type", "Transmission", "Mileage", "Status"};
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }

            List<Vehicle> vehicles = vehicleRepository.findAll();
            int rowNum = 1;
            for (Vehicle vehicle : vehicles) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(vehicle.getId());
                row.createCell(1).setCellValue(vehicle.getRegistrationNumber());
                row.createCell(2).setCellValue(vehicle.getModel().getName());
                row.createCell(3).setCellValue(vehicle.getColor());
                row.createCell(4).setCellValue(vehicle.getYear());
                row.createCell(5).setCellValue(vehicle.getFuelType().name());
                row.createCell(6).setCellValue(vehicle.getTransmission().name());
                row.createCell(7).setCellValue(vehicle.getMileage());
                row.createCell(8).setCellValue(vehicle.getStatus().name());
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            workbook.write(baos);
            return baos.toByteArray();
        } catch (IOException e) {
            log.error("Error generating Excel: {}", e.getMessage());
        }
        return new byte[0];
    }
}
