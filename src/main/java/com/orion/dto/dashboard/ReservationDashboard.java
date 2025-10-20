package com.orion.dto.dashboard;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReservationDashboard {
    private String fullName;
    private String email;
    private String phoneNumber;
    private Long customerId;
    private Long totalReservations;
    private Double totalAmount;
    private Long completedReservations;
    private Long pendingReservations;
    private Long canceledReservations;
    private Double completedAmount;
    private Double pendingAmount;
    private Long waitingForPayment;
    private Long onGoing;
}
