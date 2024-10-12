package com.orion.dto.dashboard;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDashboard {
    private Long totalBookings;
    private Long totalCustomers;
    private Long successfulPayments;
    private Long processedPayments;
    private Long pendingPayments;
    private Long failedPayments;
    private Long totalPayments;
}
