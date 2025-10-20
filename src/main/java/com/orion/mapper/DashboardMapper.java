package com.orion.mapper;
import com.orion.dto.dashboard.PaymentDashboard;
import com.orion.dto.dashboard.ReservationDashboard;
import com.orion.dto.dashboard.VehicleDashboard;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class DashboardMapper {

    public List<ReservationDashboard> mapDashboard(List<Object[]> resultList) {
        List<ReservationDashboard> rentalDashboardList = new ArrayList<>();

        for (Object[] objects : resultList) {
            ReservationDashboard dashboard = new ReservationDashboard();
            String fullName = "";

            if (objects[0] != null) {
                fullName += String.valueOf(objects[0]);
            }

            if (objects[1] != null) {
                fullName += " " + objects[1];
            }

            dashboard.setFullName(fullName);

            if (objects[2] != null) {
                dashboard.setEmail(String.valueOf(objects[2]));
            }

            if (objects[3] != null) {
                dashboard.setPhoneNumber(String.valueOf(String.valueOf(objects[3])));
            }

            if (objects[4] != null) {
                dashboard.setCustomerId(Long.valueOf(String.valueOf(objects[4])));
            }

            if (objects[5] != null) {
                dashboard.setTotalReservations(Long.valueOf(String.valueOf(objects[5])));
            }

            if (objects[6] != null) {
                dashboard.setTotalAmount(Double.valueOf(String.valueOf(objects[6])));
            }

            if (objects[7] != null) {
                dashboard.setCompletedReservations(Long.valueOf(String.valueOf(objects[7])));
            }

            if (objects[8] != null) {
                dashboard.setPendingReservations(Long.valueOf(String.valueOf(objects[8])));
            }

            if (objects[9] != null) {
                dashboard.setCanceledReservations(Long.valueOf(String.valueOf(objects[9])));
            }

            if (objects[10] != null) {
                dashboard.setCompletedAmount(Double.valueOf(String.valueOf(objects[10])));
            }

            if (objects[11] != null) {
                dashboard.setPendingAmount(Double.valueOf(String.valueOf(objects[11])));
            }

            if (objects[12] != null) {
                dashboard.setWaitingForPayment(Long.valueOf(String.valueOf(objects[12])));
            }

            if (objects[13] != null) {
                dashboard.setOnGoing(Long.valueOf(String.valueOf(objects[13])));
            }

            rentalDashboardList.add(dashboard);
        }

        return rentalDashboardList;
    }

    public List<VehicleDashboard> mapVehicleDashboard(List<Object[]> resultList) {
        List<VehicleDashboard> vehicleDashboardList = new ArrayList<>();

        for (Object[] objects : resultList) {
            VehicleDashboard dashboard = new VehicleDashboard();

            if (objects[0] != null) {
                dashboard.setTotalVehicles(Long.valueOf(String.valueOf(objects[0])));
            }

            if (objects[1] != null) {
                dashboard.setAvailableVehicles(Long.valueOf(String.valueOf(objects[1])));
            }

            if (objects[2] != null) {
                dashboard.setWaitingToStart(Long.valueOf(String.valueOf(objects[2])));
            }

            if (objects[3] != null) {
                dashboard.setRentedVehicles(Long.valueOf(String.valueOf(objects[3])));
            }

            if (objects[4] != null) {
                dashboard.setUnderMaintenance(Long.valueOf(String.valueOf(objects[4])));
            }

            if (objects[5] != null) {
                dashboard.setReservedVehicles(Long.valueOf(String.valueOf(objects[5])));
            }

            if (objects[6] != null) {
                dashboard.setOutOfServiceVehicles(Long.valueOf(String.valueOf(objects[6])));
            }

            if (objects[7] != null) {
                dashboard.setCategory(String.valueOf(String.valueOf(objects[7])));
            }

            if (objects[8] != null) {
                dashboard.setCategoryCount(Long.valueOf(String.valueOf(objects[8])));
            }

            vehicleDashboardList.add(dashboard);
        }

        return vehicleDashboardList;
    }

    public List<PaymentDashboard> mapPaymentDashboard(List<Object[]> resultList) {
        List<PaymentDashboard> paymentDashboardList = new ArrayList<>();

        for (Object[] objects : resultList) {
            PaymentDashboard bookingDashboard = new PaymentDashboard();

            if (objects[0] != null) {
                bookingDashboard.setTotalBookings(Long.valueOf(String.valueOf(objects[0])));
            }

            if (objects[1] != null) {
                bookingDashboard.setTotalCustomers(Long.valueOf(String.valueOf(objects[1])));
            }

            if (objects[2] != null) {
                bookingDashboard.setSuccessfulPayments(Long.valueOf(String.valueOf(objects[2])));
            }

            if (objects[3] != null) {
                bookingDashboard.setProcessedPayments(Long.valueOf(String.valueOf(objects[3])));
            }

            if (objects[4] != null) {
                bookingDashboard.setPendingPayments(Long.valueOf(String.valueOf(objects[4])));
            }

            if (objects[5] != null) {
                bookingDashboard.setFailedPayments(Long.valueOf(String.valueOf(objects[5])));
            }

            if (objects[6] != null) {
                bookingDashboard.setTotalPayments(Long.valueOf(String.valueOf(objects[6])));
            }

            paymentDashboardList.add(bookingDashboard);
        }

        return paymentDashboardList;
    }

}
