package com.orion.service.dashboard;

import com.orion.dto.dashboard.*;
import com.orion.entity.User;
import com.orion.generics.ResponseObject;
import com.orion.repository.nativeQuery.DashboardNativeQueryRepository;
import com.orion.security.CustomUserDetails;
import com.orion.service.BaseService;
import com.orion.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class DashboardService extends BaseService {

    private final UserService userService;
    private final DashboardNativeQueryRepository dashboardNativeQueryRepository;

    public ResponseObject getDashboardRentalData(CustomUserDetails userDetails, InfoDashboard fromTo) {
        String methodName = "getDashboardRentalData";
        log.info("Fetching Rental Dashboard Data", methodName);
        ResponseObject responseObject = new ResponseObject();
        User user = userService.findByEmail(userDetails.getUsername());
        List<Long> userIds = userService.getCustomerIdsBasedOnRole(user);
        List<ReservationDashboard> rentalInfo = dashboardNativeQueryRepository.getRentalDashboard(fromTo, userIds);

        responseObject.setData(rentalInfo);
        responseObject.prepareHttpStatus(HttpStatus.OK);
        log.info("Rental Dashboard data fetched successfully");

        return responseObject;

    }

    public ResponseObject getVehiclesDashboardData(CustomUserDetails userDetails, InfoDashboard fromTo) {
        String methodName = "getVehiclesDashboardData";
        log.info("Fetching Vehicle Dashboard Data", methodName);
        ResponseObject responseObject = new ResponseObject();
        User user = userService.findByEmail(userDetails.getUsername());
        List<Long> userIds = userService.getUserIdsBasedOnRole(user);
        List<VehicleDashboard> leaderBoardList = dashboardNativeQueryRepository.getVehicleDashboard(fromTo, userIds);

        responseObject.setData(leaderBoardList);
        responseObject.prepareHttpStatus(HttpStatus.OK);
        log.info("Vehicle Dashboard data fetched successfully");

        return responseObject;

    }

    public ResponseObject getPaymentDashboardData(CustomUserDetails userDetails, InfoDashboard fromTo) {
        String methodName = "getPaymentDashboardData";
        log.info("Fetching Payment Dashboard Data", methodName);
        ResponseObject responseObject = new ResponseObject();

        User user = userService.findByEmail(userDetails.getUsername());
        List<Long> userIds = userService.getCustomerIdsBasedOnRole(user);
        List<PaymentDashboard> paymentDashboard = dashboardNativeQueryRepository.getPaymentDashboard(fromTo, userIds);


        responseObject.setData(paymentDashboard);
        responseObject.prepareHttpStatus(HttpStatus.OK);
        log.info("Payment Dashboard data fetched successfully");

        return responseObject;
    }
}
