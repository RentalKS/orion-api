package com.orion.service.reports;

import com.orion.dto.dashboard.InfoDashboard;
import com.orion.dto.raportData.ReportData;
import com.orion.dto.raportData.ReportInfo;
import com.orion.entity.Booking;
import com.orion.entity.User;
import com.orion.enums.vehicle.RentalStatus;
import com.orion.generics.ResponseObject;
import com.orion.repository.BookingRepository;
import com.orion.repository.RentalRepository;
import com.orion.service.BaseService;
import com.orion.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.*;
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
public class ReportingService extends BaseService {

    private final BookingRepository bookingRepository;
    private final RentalRepository rentalRepository;
    private final UserService userService;

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

        //TODO: Implement overdue rentals
        List<String> overdueRentals = bookings.stream()
                .filter(b -> b.getEndDate().isBefore(LocalDateTime.now()))
                .map(b -> "Booking ID: " + b.getId() + ", Customer: " + b.getCustomer().getName())
                .collect(Collectors.toList());

        return new ReportData(totalBookings, totalRevenue, mostRentedVehicles, bookingsPerCustomer, revenuePerVehicle, overdueRentals);
    }
    public byte[] generateMonthlyReportExcel(ReportInfo reportInfo, String currentEmail) {
        List<String> userEmails;
        User user = userService.findByEmail(currentEmail);

        if (isTenant(user)) {
            userEmails = userService.findUserEmailsByTenant(user.getTenant().getId());
        } else {
            userEmails = List.of(currentEmail);
        }

        List<Booking> bookings = bookingRepository.findBookingsBetweenDates(
                reportInfo.getFrom(), reportInfo.getTo(),
                reportInfo.getRentalStatus(), reportInfo.getVehicleStatus(),
                userEmails
        );

        ReportData reportData = compileReportData(bookings);

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Monthly Report");

            // Styles
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dataStyle = createDataStyle(workbook);

            int rowNum = 0;

            Row headerRow = sheet.createRow(rowNum++);
            createCell(headerRow, 0, "Report Summary", headerStyle);

            Row totalBookingsRow = sheet.createRow(rowNum++);
            createCell(totalBookingsRow, 0, "Total Bookings", headerStyle);
            createCell(totalBookingsRow, 1, String.valueOf(reportData.getTotalBookings()), dataStyle);

            Row totalRevenueRow = sheet.createRow(rowNum++);
            createCell(totalRevenueRow, 0, "Total Revenue", headerStyle);
            createCell(totalRevenueRow, 1, String.format("%.2f", reportData.getTotalRevenue()), dataStyle);

            rowNum++;

            Row vehiclesHeader = sheet.createRow(rowNum++);
            createCell(vehiclesHeader, 0, "Most Rented Vehicles", headerStyle);
            createCell(vehiclesHeader, 1, "Number of Rentals", headerStyle);

            for (String vehicle : reportData.getMostRentedVehicles()) {
                Row row = sheet.createRow(rowNum++);
                createCell(row, 0, vehicle, dataStyle);
            }

            rowNum++;

            Row customerHeader = sheet.createRow(rowNum++);
            createCell(customerHeader, 0, "Customer", headerStyle);
            createCell(customerHeader, 1, "Number of Bookings", headerStyle);

            for (Map.Entry<String, Integer> entry : reportData.getBookingsPerCustomer().entrySet()) {
                Row row = sheet.createRow(rowNum++);
                createCell(row, 0, entry.getKey(), dataStyle);
                createCell(row, 1, String.valueOf(entry.getValue()), dataStyle);
            }

            rowNum++;

            Row revenueHeader = sheet.createRow(rowNum++);
            createCell(revenueHeader, 0, "Vehicle", headerStyle);
            createCell(revenueHeader, 1, "Total Revenue", headerStyle);

            for (Map.Entry<String, Double> entry : reportData.getRevenuePerVehicle().entrySet()) {
                Row row = sheet.createRow(rowNum++);
                createCell(row, 0, entry.getKey(), dataStyle);
                createCell(row, 1, String.format("%.2f", entry.getValue()), dataStyle);
            }

            rowNum++;

            Row overdueHeader = sheet.createRow(rowNum++);
            createCell(overdueHeader, 0, "Overdue Rentals", headerStyle);

            for (String overdueRental : reportData.getOverdueRentals()) {
                Row row = sheet.createRow(rowNum++);
                createCell(row, 0, overdueRental, dataStyle);
            }
            for (int i = 0; i < 2; i++) {
                sheet.autoSizeColumn(i);
            }
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                workbook.write(outputStream);
                return outputStream.toByteArray();
            }
        } catch (IOException e) {
            log.error("Error generating Excel report", e);
            return null;
        }
    }
    private void createCell(Row row, int column, String value, CellStyle style) {
        Cell cell = row.createCell(column);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }
    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        return style;
    }
    private CellStyle createDataStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 11);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        return style;
    }

}
