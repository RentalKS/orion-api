package com.orion.mapper;

import com.orion.dto.dashboard.ReservationDashboard;
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

            if (objects[0] != null)
                fullName += String.valueOf(objects[0]);

            if (objects[1] != null)
                fullName += " " + objects[1];

            dashboard.setFullName(fullName);

            if (objects[2] != null)
                dashboard.setEmail(String.valueOf(objects[2]));

            if (objects[3] != null)
                dashboard.setPhoneNumber(String.valueOf(String.valueOf(objects[3])));

            if (objects[4] != null)
                dashboard.setCustomerId(Long.valueOf(String.valueOf(objects[4])));

            if (objects[5] != null)
                dashboard.setTotalReservations(Long.valueOf(String.valueOf(objects[5])));

            if (objects[6] != null)
                dashboard.setTotalAmount(Double.valueOf(String.valueOf(objects[6])));

            if (objects[7] != null)
                dashboard.setCompletedReservations(Long.valueOf(String.valueOf(objects[7])));

            if (objects[8] != null)
                dashboard.setPendingReservations(Long.valueOf(String.valueOf(objects[8])));

            if (objects[9] != null)
                dashboard.setCanceledReservations(Long.valueOf(String.valueOf(objects[9])));

            if (objects[10] != null)
                dashboard.setCompletedAmount(Double.valueOf(String.valueOf(objects[10])));

            if (objects[11] != null)
                dashboard.setPendingAmount(Double.valueOf(String.valueOf(objects[11])));

            if (objects[12] != null)
                dashboard.setWaitingForPayment(Long.valueOf(String.valueOf(objects[12])));

            if (objects[13] != null)
                dashboard.setOnGoing(Long.valueOf(String.valueOf(objects[13])));

            rentalDashboardList.add(dashboard);
        }

        return rentalDashboardList;
    }
}
