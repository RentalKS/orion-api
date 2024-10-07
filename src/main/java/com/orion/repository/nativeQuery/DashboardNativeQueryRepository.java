package com.orion.repository.nativeQuery;

import com.orion.enums.vehicle.RentalStatus;
import com.orion.infrastructure.tenant.ConfigSystem;
import com.orion.dto.dashboard.InfoDashboard;
import com.orion.dto.dashboard.ReservationDashboard;
import com.orion.dto.vehicle.VehicleDashboard;
import com.orion.mapper.DashboardMapper;
import com.orion.util.DateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class DashboardNativeQueryRepository {
    private final EntityManager entityManager;
    private final DashboardMapper dashboardMapper;


    public List<ReservationDashboard> getRentalDashboard(InfoDashboard infoDashboard, List<Long> userIds) {
        Long tenantId = ConfigSystem.getTenant().getId();

        String where = " u.tenant_id=" + tenantId + " ";

        String fromDate = null;
        String toDate = null;

        if (infoDashboard.getFrom() != null) {
            fromDate = String.valueOf(DateUtil.convertToLocalDateTime(infoDashboard.getFrom()));
        }
        if (infoDashboard.getTo() != null) {
            toDate = String.valueOf(DateUtil.convertToLocalDateTime(infoDashboard.getTo()));
        }

        where += " AND r.created_at BETWEEN '" + fromDate + "' AND '" + toDate + "'";

        if (userIds != null) {
            String memberIdList = userIds.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(",", "(", ")"));
            where += " AND c.id IN " + memberIdList;
        }

        String queryString = "SELECT c.name, c.last_name, c.email, c.phone_number, c.id, " +
                "COUNT(DISTINCT r.id) as totalReservations, " +
                "SUM(r.total_cost) as totalAmount, " +
                "SUM(CASE WHEN r.status = '" + RentalStatus.COMPLETED + "' THEN 1 ELSE 0 END) as completedReservations, " +
                "SUM(CASE WHEN r.status = '" + RentalStatus.PENDING + "' THEN 1 ELSE 0 END) as pendingReservations, " +
                "SUM(CASE WHEN r.status = '" + RentalStatus.CANCELLED + "' THEN 1 ELSE 0 END) as canceledReservations, " +
                "SUM(CASE WHEN r.status = '" + RentalStatus.COMPLETED + "' THEN r.total_cost ELSE 0 END) as completedAmount, " +
                "SUM(CASE WHEN r.status = '" + RentalStatus.PENDING + "' THEN r.total_cost ELSE 0 END) as pendingAmount, " +
                "SUM(CASE WHEN r.status = '" + RentalStatus.WAITING_FOR_PAYMENT + "' THEN 1 ELSE 0 END) as waitingForPayment " +
                "FROM customers c " +
                "LEFT JOIN rentals r ON c.id = r.customer_id " +
                "WHERE " + where +
                "AND c.deleted_at is null AND r.deleted_at is null " +
                "GROUP BY u.name, u.last_name, u.email, u.phone_number, u.id";

        Query query = entityManager.createNativeQuery(queryString);

        List<Object[]> resultList = query.getResultList();
        return dashboardMapper.mapDashboard(resultList);
    }


    public List<VehicleDashboard> getVehicleDashboard(InfoDashboard infoDashboard, List<Long> userIds) {
        Long tenantId = ConfigSystem.getTenant().getId();

        String where = " v.tenant_id=" + tenantId + " ";

        String fromDate = null;
        String toDate = null;

        if (infoDashboard.getFrom() != null) {
            fromDate = String.valueOf(DateUtil.convertToLocalDateTime(infoDashboard.getFrom()));
        }
        if (infoDashboard.getTo() != null) {
            toDate = String.valueOf(DateUtil.convertToLocalDateTime(infoDashboard.getTo()));
        }

        if (userIds != null) {
            String memberIdList = userIds.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(",", "(", ")"));
            where += " AND u.id IN " + memberIdList;
        }


        String queryString = "SELECT " +
                "COUNT(v.id) as totalVehicles, " +
                "SUM(CASE WHEN v.status = 'RENTED' THEN 1 ELSE 0 END) as rentedVehicles, " +
                "SUM(CASE WHEN v.status = 'UNDER_MAINTENANCE' THEN 1 ELSE 0 END) as underMaintenance, " +
                "SUM(CASE WHEN v.status = 'RESERVED' THEN 1 ELSE 0 END) as reserved, " +
                "SUM(CASE WHEN v.status = 'OUT_OF_SERVICE' THEN 1 ELSE 0 END) as outOfService, " +
                "c.category_name as category, COUNT(c.id) as categoryCount " +
                "FROM vehicles v " +
                "LEFT JOIN sections s on v.section_id = s.id LEFT JOIN categories c ON s.category_id = c.id " +
                "LEFT JOIN user u on c.user_id = u.id " +
                "WHERE " + where + " and v.deleted_at is null " +
                "GROUP BY c.category_name";

        Query query = entityManager.createNativeQuery(queryString);
        List<Object[]> resultList = query.getResultList();
        return mapVehicleDashboard(resultList);
    }

    private List<VehicleDashboard> mapVehicleDashboard(List<Object[]> resultList) {
        List<VehicleDashboard> vehicleDashboardList = new ArrayList<>();

        for (Object[] objects : resultList) {
            VehicleDashboard dashboard = new VehicleDashboard();

            if(objects[0] != null)
                dashboard.setTotalVehicles(Long.valueOf(String.valueOf(objects[0])));

            if(objects[1] != null)
                dashboard.setRentedVehicles(Long.valueOf(String.valueOf(objects[1])));

            if(objects[2] != null)
                dashboard.setUnderMaintenance(Long.valueOf(String.valueOf(objects[2])));

            if(objects[3] != null)
                dashboard.setReservedVehicles(Long.valueOf(String.valueOf(objects[3])));

            if(objects[4] != null)
                dashboard.setOutOfServiceVehicles(Long.valueOf(String.valueOf(objects[4])));

            if(objects[5] != null)
                dashboard.setCategoryCount(Long.valueOf(String.valueOf(objects[5])));

            vehicleDashboardList.add(dashboard);
        }

        return vehicleDashboardList;
    }


}
