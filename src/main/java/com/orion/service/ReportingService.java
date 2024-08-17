package com.orion.service;

import com.orion.dto.raportData.ReportData;
import com.orion.entity.Booking;
import com.orion.enums.vehicle.RentalStatus;
import com.orion.generics.ResponseObject;
import com.orion.repository.BookingRepository;
import com.orion.repository.RentalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class ReportingService {

    private final BookingRepository bookingRepository;
    private final RentalRepository rentalRepository;

    public ResponseObject generateMonthlyReport() {
        String methodName = "generateMonthlyReport";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        LocalDateTime startDate = LocalDateTime.now().minusMonths(1);
        LocalDateTime endDate = LocalDateTime.now();
        List<Booking> bookings = bookingRepository.findBookingsBetweenDates(startDate, endDate);

        ReportData reportData = compileReportData(bookings);

        responseObject.setData(reportData);
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }

    private ReportData compileReportData(List<Booking> bookings) {
        int totalBookings = bookings.size();
        double totalRevenue = bookings.stream()
            .mapToDouble(booking -> {
                Double revenue = rentalRepository.findTotalRevenueByBookingId(booking.getId());
                return revenue != null ? revenue : 0.0;
            })
            .sum();

        List<String> mostRentedVehicles = bookings.stream()
                .collect(Collectors.groupingBy(b -> b.getVehicle().getModel().getName(), Collectors.counting()))
                .entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .map(Map.Entry::getKey)
                .limit(5)
                .collect(Collectors.toList());

        Map<String, Integer> bookingsPerCustomer = bookings.stream()
                .collect(Collectors.groupingBy(b -> b.getCustomer().getName(), Collectors.summingInt(b -> 1)));

        Map<String, Double> revenuePerVehicle = bookings.stream()
            .collect(Collectors.groupingBy(
                b -> b.getVehicle().getModel().getName(),
                Collectors.summingDouble(b -> {
                    Double revenue = rentalRepository.findTotalRevenueByBookingId(b.getId());
                    return revenue != null ? revenue : 0.0;
                })
            ));

        List<String> overdueRentals = bookings.stream()
                .filter(b -> b.getEndDate().isBefore(LocalDateTime.now()) && b.getStatus() != RentalStatus.COMPLETED)
                .map(b -> "Booking ID: " + b.getId() + ", Customer: " + b.getCustomer().getName())
                .collect(Collectors.toList());

        return new ReportData(totalBookings, totalRevenue, mostRentedVehicles, bookingsPerCustomer, revenuePerVehicle, overdueRentals);
    }

    public byte[] generateMonthlyReportExcel() {
        LocalDateTime startDate = LocalDateTime.now().minusMonths(1);
        LocalDateTime endDate = LocalDateTime.now();
        List<Booking> bookings = bookingRepository.findBookingsBetweenDates(startDate, endDate);

        ReportData reportData = compileReportData(bookings);

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Monthly Report");

            // Create header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Total Bookings");
            headerRow.createCell(1).setCellValue("Total Revenue");
            headerRow.createCell(2).setCellValue("Most Rented Vehicles");
            headerRow.createCell(3).setCellValue("Bookings Per Customer");
            headerRow.createCell(4).setCellValue("Revenue Per Vehicle");
            headerRow.createCell(5).setCellValue("Overdue Rentals");

            Row dataRow = sheet.createRow(1);
            dataRow.createCell(0).setCellValue(reportData.getTotalBookings());
            dataRow.createCell(1).setCellValue(reportData.getTotalRevenue());
            dataRow.createCell(2).setCellValue(String.join(", ", reportData.getMostRentedVehicles()));

            String bookingsPerCustomerFormatted = reportData.getBookingsPerCustomer().entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining(", "));

            dataRow.createCell(3).setCellValue(bookingsPerCustomerFormatted);

            String revenuePerVehicleFormatted = reportData.getRevenuePerVehicle().entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining(", "));

            dataRow.createCell(4).setCellValue(revenuePerVehicleFormatted);

            dataRow.createCell(5).setCellValue(String.join(", ", reportData.getOverdueRentals()));

            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                workbook.write(outputStream);
                return outputStream.toByteArray();
            }
        } catch (IOException e) {
            log.error("Error generating Excel report", e);
            return null;
        }
    }
}
