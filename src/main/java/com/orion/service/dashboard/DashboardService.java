package com.orion.service.dashboard;

import com.orion.dto.dashboard.InfoDashboard;
import com.orion.dto.dashboard.ReservationDashboard;
import com.orion.dto.vehicle.VehicleDashboard;
import com.orion.entity.User;
import com.orion.exception.InternalException;
import com.orion.generics.ResponseObject;
import com.orion.repository.UserRepository;
import com.orion.repository.nativeQuery.DashboardNativeQueryRepository;
import com.orion.security.CustomUserDetails;
import com.orion.service.BaseService;
import com.orion.service.customer.CustomerService;
import com.orion.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class DashboardService extends BaseService {
    private final UserService userService;
    private final DashboardNativeQueryRepository dashboardNativeQueryRepository;
    private final CustomerService customerService;

    public ResponseObject getDashboardRentalData(CustomUserDetails userDetails, InfoDashboard fromTo) {
        String methodName = "getDashboardData";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        User user = userService.findByEmail(userDetails.getUsername());

        try {
            List<Long> userIds = userService.getUserIdsBasedOnRole(user);
            List<ReservationDashboard> rentalInfo = dashboardNativeQueryRepository.getRentalDashboard(fromTo, userIds);

            responseObject.setData(rentalInfo);
            responseObject.prepareHttpStatus(HttpStatus.OK);

        } catch (Exception e) {
            log.error("{} -> Error fetching dashboard data: {}", methodName, e.getMessage(), e);
            throw new InternalException(e.getLocalizedMessage(), e.getCause());
        }

        log.info("{} -> Total Rental Dashboard And Sales For Tenant, response status: {}", methodName, responseObject.getCode());
        return responseObject;
    }
    public ResponseObject getVehiclesDashboardData(CustomUserDetails userDetails, InfoDashboard fromTo) {
        String methodName = "getDashboardData";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        User user = userService.findByEmail(userDetails.getUsername());

        try {
            List<Long> userIds = userService.getUserIdsBasedOnRole(user);
            List<VehicleDashboard> leaderBoardList = dashboardNativeQueryRepository.getVehicleDashboard(fromTo, userIds);

            responseObject.setData(null);
            responseObject.prepareHttpStatus(HttpStatus.OK);

        }catch (Exception e) {
            log.error("{} -> Total Rental And Dashboard For Tenant", methodName);
            throw new InternalException(e.getLocalizedMessage(), e.getCause());
        }
        log.info("{} -> Total Rental Dashboard And Sales For Tenant, response status: {}", methodName,
                responseObject.getCode());
        return responseObject;
    }
}
