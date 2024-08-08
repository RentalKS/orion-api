package com.orion.service;

import com.orion.generics.ResponseObject;
import com.orion.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Log4j2
public class ReportingService {

    private final BookingRepository bookingRepository;

    public ResponseObject generateMonthlyReport() {
        String methodName = "generateMonthlyReport";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        LocalDateTime startDate = LocalDateTime.now().minusMonths(1);
        LocalDateTime endDate = LocalDateTime.now();
//        List<Booking> bookings = bookingRepository.findBookingsBetweenDates(startDate, endDate);

        // Logic to compile and analyze report data
//        ReportData reportData = compileReportData(bookings);

//        responseObject.setData(reportData);
        responseObject.prepareHttpStatus(HttpStatus.OK);
        return responseObject;
    }

//    private ReportData compileReportData(List<Booking> bookings) {
//        int totalBookings = bookings.size();
//        double totalRevenue = bookings.stream().mapToDouble(Booking::getTotalCost).sum();
//
//        List<Model> mostRentedVehicles = bookings.stream()
//                .collect(Collectors.groupingBy(b -> b.getVehicle().getModel(), Collectors.counting()))
//                .entrySet().stream()
//                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
//                .map(Map.Entry::getKey)
//                .limit(5)
//                .collect(Collectors.toList());
//
//        Map<String, Integer> bookingsPerCustomer = bookings.stream()
//                .collect(Collectors.groupingBy(b -> b.getCustomer().getName(), Collectors.summingInt(b -> 1)));
//
//        Map<String, Double> revenuePerVehicle = bookings.stream()
//                .collect(Collectors.groupingBy(b -> b.getVehicle().getModel(), Collectors.summingDouble(Booking::getTotalCost)));
//
//        List<String> overdueRentals = bookings.stream()
//                .filter(b -> b.getEndDate().isBefore(LocalDateTime.now()) && b.getStatus() != RentalStatus.COMPLETED)
//                .map(b -> "Booking ID: " + b.getId() + ", Customer: " + b.getCustomer().getName())
//                .collect(Collectors.toList());
//
//        return new ReportData(totalBookings, totalRevenue, mostRentedVehicles, bookingsPerCustomer, revenuePerVehicle, overdueRentals);
//    }
}