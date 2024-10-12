package com.orion.repository.nativeQuery;
import com.orion.dto.dashboard.PaymentDashboard;
import com.orion.enums.payment.PaymentStatus;
import com.orion.enums.vehicle.RentalStatus;
import com.orion.enums.vehicle.VehicleStatus;
import com.orion.infrastructure.tenant.ConfigSystem;
import com.orion.dto.dashboard.InfoDashboard;
import com.orion.dto.dashboard.ReservationDashboard;
import com.orion.dto.dashboard.VehicleDashboard;
import com.orion.mapper.DashboardMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class DashboardNativeQueryRepository {
    private final EntityManager entityManager;
    private final DashboardMapper dashboardMapper;
    public List<ReservationDashboard> getRentalDashboard(InfoDashboard infoDashboard, List<Long> userIds) {
        Long tenantId = ConfigSystem.getTenant().getId();

        String where = " c.tenant_id=" + tenantId + " ";

        if (infoDashboard.getFrom() != null && infoDashboard.getTo() != null){
            where += " AND r.created_at BETWEEN '" + infoDashboard.getFrom() + "' AND '" + infoDashboard.getTo() + "'";
        }

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
                "SUM(CASE WHEN r.status = '" + RentalStatus.WAITING_FOR_PAYMENT + "' THEN 1 ELSE 0 END) as waitingForPayment, " +
                "SUM(CASE WHEN r.status = '" + RentalStatus.ONGOING + "' THEN 1 ELSE 0 END) as onGoing " +
                "FROM customers c " +
                "LEFT JOIN rentals r ON c.id = r.customer_id " +
                "WHERE " + where +
                "AND c.deleted_at is null AND r.deleted_at is null " +
                "GROUP BY c.name, c.last_name, c.email, c.phone_number, c.id";

        Query query = entityManager.createNativeQuery(queryString);

        List<Object[]> resultList = query.getResultList();
        return dashboardMapper.mapDashboard(resultList);
    }


    public List<VehicleDashboard> getVehicleDashboard(InfoDashboard infoDashboard, List<Long> userIds) {
        Long tenantId = ConfigSystem.getTenant().getId();

        String where = " v.tenant_id=" + tenantId + " ";
        if(infoDashboard.getFrom() != null && infoDashboard.getTo() != null){
            where += " AND v.created_at BETWEEN '" + infoDashboard.getFrom() + "' AND '" + infoDashboard.getTo() + "'";
        }

        if (userIds != null) {
            String memberIdList = userIds.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(",", "(", ")"));
            where += " AND u.id IN " + memberIdList;
        }

        String queryString = "SELECT " +
                "COUNT(v.id) as totalVehicles, " +
                "SUM(CASE WHEN v.status = '" + VehicleStatus.AVAILABLE + "' THEN 1 ELSE 0 END) as availableVehicles, " +
                "SUM(CASE WHEN v.status = '" + VehicleStatus.WAITING_TO_START + "' THEN 1 ELSE 0 END) as waitingToStart, " +
                "SUM(CASE WHEN v.status = '" + VehicleStatus.RENTED + "' THEN 1 ELSE 0 END) as rentedVehicles, " +
                "SUM(CASE WHEN v.status = '" + VehicleStatus.UNDER_MAINTENANCE + "' THEN 1 ELSE 0 END) as underMaintenance, " +
                "SUM(CASE WHEN v.status = '" + VehicleStatus.RESERVED + "' THEN 1 ELSE 0 END) as reserved, " +
                "SUM(CASE WHEN v.status = '" + VehicleStatus.OUT_OF_SERVICE + "' THEN 1 ELSE 0 END) as outOfService, " +
                "c.category_name as category, " +
                "COUNT(c.id) as categoryCount " +
                "FROM vehicles v " +
                "LEFT JOIN sections s on v.section_id = s.id LEFT JOIN categories c ON s.category_id = c.id " +
                "LEFT JOIN user u ON v.user_id = u.id " +
                "WHERE " + where + " and v.deleted_at is null " +
                "GROUP BY c.id";

        Query query = entityManager.createNativeQuery(queryString);
        List<Object[]> resultList = query.getResultList();
        return dashboardMapper.mapVehicleDashboard(resultList);
    }

    public List<PaymentDashboard> getPaymentDashboard(InfoDashboard infoDashboard, List<Long> userIds) {
        Long tenantId = ConfigSystem.getTenant().getId();
        String where = " b.tenant_id=" + tenantId + " ";

        if (infoDashboard.getFrom() != null && infoDashboard.getTo() != null) {
            where += " AND b.created_at BETWEEN '" + infoDashboard.getFrom() + "' AND '" + infoDashboard.getTo() + "'";
        }

        if (userIds != null) {
            String memberIdList = userIds.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(",", "(", ")"));
            where += " AND c.id IN " + memberIdList;
        }

        String queryString = "SELECT " +
                "COUNT(DISTINCT b.id) AS totalBookings, " +
                "COUNT(DISTINCT c.id) AS totalCustomers, " +
                "SUM(CASE WHEN p.payment_status = '" + PaymentStatus.SUCCESS + "' THEN 1 ELSE 0 END) AS successfulPayments, " +
                "SUM(CASE WHEN p.payment_status = '" + PaymentStatus.PROCESSING + "' THEN 1 ELSE 0 END) AS processedPayments, " +
                "SUM(CASE WHEN p.payment_status = '" + PaymentStatus.PENDING + "' THEN 1 ELSE 0 END) AS pendingPayments, " +
                "SUM(CASE WHEN p.payment_status = '" + PaymentStatus.FAILED + "' THEN 1 ELSE 0 END) AS failedPayments, " +
                "COUNT(p.id) AS totalPayments " +
                "FROM bookings b " +
                "LEFT JOIN customers c ON b.customer_id = c.id " +
                "LEFT JOIN vehicles v ON b.vehicle_id = v.id " +
                "LEFT JOIN rentals r ON r.customer_id = c.id " +
                "LEFT JOIN payments p ON p.rental_id = r.id " +
                "WHERE " + where + " AND b.deleted_at IS NULL ";
        Query query = entityManager.createNativeQuery(queryString);
        List<Object[]> resultList = query.getResultList();

        return dashboardMapper.mapPaymentDashboard(resultList);
    }
}
