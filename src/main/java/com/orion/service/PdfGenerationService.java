package com.orion.service;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import com.orion.entity.Vehicle;
import com.orion.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class PdfGenerationService {

    private final VehicleRepository vehicleRepository;

    public byte[] generateVehicleReport() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
            document.add(new Paragraph("Vehicle Report").setFont(font).setFontSize(18));

            List<Vehicle> vehicles = vehicleRepository.findAll();
            Table table = new Table(UnitValue.createPercentArray(new float[]{2, 2, 2, 2, 2, 2, 2, 2, 2})).useAllAvailableWidth();
            table.addHeaderCell("ID");
            table.addHeaderCell("Registration Number");
            table.addHeaderCell("Model");
            table.addHeaderCell("Color");
            table.addHeaderCell("Year");
            table.addHeaderCell("Fuel Type");
            table.addHeaderCell("Transmission");
            table.addHeaderCell("Mileage");
            table.addHeaderCell("Status");

            for (Vehicle vehicle : vehicles) {
                table.addCell(String.valueOf(vehicle.getId()));
                table.addCell(vehicle.getRegistrationNumber());
                table.addCell(vehicle.getModel().getName());
                table.addCell(vehicle.getColor());
                table.addCell(vehicle.getYear());
                table.addCell(vehicle.getFuelType().name());
                table.addCell(vehicle.getTransmission().name());
                table.addCell(vehicle.getMileage().toString());
//                table.addCell(vehicle.getStatus().name());

            }

            document.add(table);
            document.close();
        } catch (Exception e) {
            log.error("Error generating PDF: {}", e.getMessage());
        }

        return baos.toByteArray();
    }
}
