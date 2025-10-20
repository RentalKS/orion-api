package com.orion.dto.raportData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportData {
    private int totalBookings;
    private double totalRevenue;
    private List<String> mostRentedVehicles;
    private Map<String, Integer> bookingsPerCustomer;
    private Map<String, Double> revenuePerVehicle;
    private List<String> overdueRentals;
}