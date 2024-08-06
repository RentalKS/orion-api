package com.orion.service;

import com.orion.dto.dashboard.InfoDashboard;
import com.orion.dto.dashboard.ReservationDashboard;
import com.orion.dto.vehicle.VehicleDashboard;
import com.orion.entity.User;
import com.orion.exception.InternalException;
import com.orion.generics.ResponseObject;
import com.orion.repository.UserRepository;
import com.orion.repository.nativeQuery.DashboardNativeQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class DashboardService extends BaseService {
    private final UserRepository userRepository;
    private final DashboardNativeQueryRepository dashboardNativeQueryRepository;

    public ResponseObject getDashboardData(UserDetails userDetails, InfoDashboard fromTo) {
        String methodName = "getDashboardData";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Optional<User> user = userRepository.findByEmail(userDetails.getUsername());
        isPresent(user);

        try {
            List<Long> roleIds = new ArrayList<>();
            roleIds.add(1L);
            roleIds.add(2L);
            roleIds.add(3L);
            roleIds.add(4L);
            roleIds.add(5L);
            roleIds.add(6L);
            Long userId;
            List<Long> userIds = new ArrayList<>();
            if(user.get().getRole().getName().equals("ADMIN)") || user.get().getRole().getName().equals("TENANT") || user.get().getRole().getName().equals("AGENCY") || user.get().getRole().getName().equals("AGENT") || user.get().getRole().getName().equals("CUSTOMER") || user.get().getRole().equals("DRIVER") || user.get().getRole().equals("MANAGER") || user.get().getRole().equals("OWNER") || user.get().getRole().equals("SUPERVISOR")) {
                userIds.add(user.get().getId());
            }

            List<ReservationDashboard> leaderBoardList = dashboardNativeQueryRepository.getDashboard(fromTo, userIds);

            responseObject.setData(leaderBoardList);
            responseObject.prepareHttpStatus(HttpStatus.OK);

        }catch (Exception e) {
            log.error("{} -> Total Rental And Dashboard For Tenant", methodName);
            throw new InternalException(e.getLocalizedMessage(), e.getCause());
        }
        log.info("{} -> Total Rental Dashboard And Sales For Tenant, response status: {}", methodName,
                responseObject.getCode());
        return responseObject;
    }

    public ResponseObject getVehiclesDashboardData(UserDetails userDetails, InfoDashboard fromTo) {
        String methodName = "getDashboardData";
        log.info("Entering: {}", methodName);
        ResponseObject responseObject = new ResponseObject();

        Optional<User> user = userRepository.findByEmail(userDetails.getUsername());
        isPresent(user);

        try {
            List<Long> roleIds = new ArrayList<>();
            roleIds.add(1L);
            roleIds.add(2L);
            roleIds.add(3L);
            roleIds.add(4L);
            roleIds.add(5L);
            roleIds.add(6L);
            Long userId;
            List<Long> userIds = new ArrayList<>();
            if(user.get().getRole().getName().equals("ADMIN)") || user.get().getRole().getName().equals("TENANT") || user.get().getRole().getName().equals("AGENCY") || user.get().getRole().getName().equals("AGENT") || user.get().getRole().getName().equals("CUSTOMER") || user.get().getRole().equals("DRIVER") || user.get().getRole().equals("MANAGER") || user.get().getRole().equals("OWNER") || user.get().getRole().equals("SUPERVISOR")) {
                userIds.add(user.get().getId());
            }

            List<VehicleDashboard> leaderBoardList = dashboardNativeQueryRepository.getVehicleDashboard(fromTo, userIds);

            responseObject.setData(leaderBoardList);
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
