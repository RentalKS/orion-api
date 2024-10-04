package com.orion.repository.nativeQuery;

import com.orion.dto.filter.VehicleFilter;
import com.orion.dto.vehicle.VehicleViewDto;
import com.orion.enums.vehicle.*;
import com.orion.util.DateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class NativeQueryRepository {
    private final EntityManager entityManager;

    public List<VehicleViewDto> filterVehicles(Long tenantId, Integer page, Integer size, VehicleFilter vehicleFilter, String memberIds){
        page = page - 1;
        int offset = page * size;
        String where =  filterConditions(tenantId, vehicleFilter, memberIds);


        String queryString = "Select " +
                " v.id, " +
                " v.created_at, " +
                " rt.name as rate_name, " +
                " rt.daily_rate, " +
                " rt.weekly_rate, " +
                " rt.monthly_rate, " +
                " v.registration_number, " +
                " v.model_id, " +
                " v.year, " +
                " v.fuel_type, " +
                " v.mileage, " +
                " v.transmission, " +
                " v.color, " +
                " v.description, " +
                " v.image, " +
                " v.location_id, " +
                " v.section_id, " +
                " s.section_name as section_name, " +
                " m.name as model_name, " +
                " m.brand as model_brand, " +
                " m.type as model_type, " +
                " m.model_image as model_image, " +
                " l.country as location_country, " +
                " l.address as location_address, " +
                " r.status as rental_status, " +
                " mr.maintenance_start_date as meintenance_date, " +
                " mr.description as meintenance_description, " +
                " mr.cost as meintenance_cost, " +
                " mr.damage_type as meintenance_damage_type, " +
                " u.first_name as created_from_name, " +
                " u.id as created_from_name " +
                " FROM vehicles v " +
                " LEFT JOIN rate_dates rt ON v.rate_dates_id = rt.id " +
                " LEFT JOIN sections s ON v.section_id = s.id " +
                " LEFT JOIN models m ON v.model_id = m.id " +
                " LEFT JOIN locations l ON v.location_id = l.id " +
                " LEFT JOIN maintenance_records mr on mr.vehicle_id = v.id " +
                " LEFT JOIN rentals r on r.vehicle_id = v.id " +
                " LEFT JOIN user u on v.user_id = u.id " +
                " WHERE " + where + " and v.deleted_at is null " +
                " GROUP BY v.id, v.created_at, rt.name, rt.daily_rate, rt.weekly_rate, rt.monthly_rate, v.registration_number, v.model_id, v.year, v.fuel_type, v.mileage, v.transmission, v.color, v.description, v.image, v.location_id, v.section_id, s.section_name, m.name, m.brand, m.type, m.model_image, l.country, l.address, r.status, mr.maintenance_date, mr.description, mr.cost, mr.damage_type,u.first_name, u.id " +
                " LIMIT " + size + " OFFSET " + offset;

        Query query = entityManager.createNativeQuery(queryString);
        List<Object[]> resultList = query.getResultList();
        return mapVehicleDto(resultList);
    }

    private String  filterConditions(Long tenantId, VehicleFilter vehicleFilter, String memberIds) {
        String where = " v.tenant_id=" + tenantId + " ";

        String fromDate = null;
        String toDate = null;

        if(vehicleFilter.getFrom() != null){
            fromDate = String.valueOf(DateUtil.convertToLocalDateTime(vehicleFilter.getFrom()));
        }
        if(vehicleFilter.getTo() != null){
            toDate = String.valueOf(DateUtil.convertToLocalDateTime(vehicleFilter.getTo()));
        }

        Long userId = vehicleFilter.getUserId();
        Long customerId = vehicleFilter.getCustomerId();


        if(fromDate != null){
            where = where + " AND (r.created_at >= '" + fromDate + "') ";
        }

        if(toDate != null){
            where = where + " AND (r.created_at <= '" + toDate + "') ";
        }

        if(vehicleFilter.getUserId() != null){
            where = where + " AND r.user_id = " + userId;
        }

        if(vehicleFilter.getCustomerId() != null){
            where = where + " AND r.customer_id = " + customerId;
        }

        if(vehicleFilter.getSearch() != null){
            where = where + " AND (v.registration_number LIKE '%" + vehicleFilter.getSearch() + "%' OR v.model LIKE '%" + vehicleFilter.getSearch() + "%')";
        }

        if(vehicleFilter.getStatus() != null){
            where = where + " AND r.status = '" + vehicleFilter.getStatus() + "'";
        }

        if(memberIds != null){
            where = where + " AND r.user_id IN " + memberIds;
        }

        if(vehicleFilter.getLocationId() != null){
            where = where + " AND r.location_id = " + vehicleFilter.getLocationId();
        }
        return where;
    }

    private List<VehicleViewDto> mapVehicleDto(List<Object[]> resultList) {
        List<VehicleViewDto> vehicleViewDtoList = new ArrayList<>();

        for (Object[] objects : resultList) {
            VehicleViewDto vehicleViewDto = new VehicleViewDto();

            if (objects[0] != null) {
                vehicleViewDto.setId(Long.valueOf(String.valueOf(objects[0])));
            }

            if (objects[1] != null) {
                Timestamp createdAt = (Timestamp) objects[1];
                vehicleViewDto.setCreatedAt(createdAt.getTime());
            }
//
//            if (objects[2] != null) {
//                vehicleViewDto.setRateName(String.valueOf(objects[2]));
//            }
//
//            if (objects[3] != null) {
//                vehicleViewDto.setDailyRate(Double.valueOf(String.valueOf(objects[3])));
//            }
//
//            if (objects[4] != null) {
//                vehicleViewDto.setWeeklyRate(Double.valueOf(String.valueOf(objects[4])));
//            }
//
//            if (objects[5] != null) {
//                vehicleViewDto.setMonthlyRate(Double.valueOf(String.valueOf(objects[5])));
//            }

            if (objects[6] != null) {
                vehicleViewDto.setRegistrationNumber(String.valueOf(objects[6]));
            }

            if (objects[7] != null) {
                vehicleViewDto.setModelId(Long.valueOf(String.valueOf(objects[7])));
            }

            if (objects[8] != null) {
                vehicleViewDto.setYear(String.valueOf(objects[8]));
            }

            if (objects[9] != null) {
                vehicleViewDto.setFuelType(FuelType.valueOf(String.valueOf(objects[9])));
            }

            if (objects[10] != null) {
                vehicleViewDto.setMileage(Long.valueOf(String.valueOf(objects[10])));
            }

            if (objects[11] != null) {
                vehicleViewDto.setTransmission(TransmissionType.valueOf(String.valueOf(objects[11])));
            }

            if (objects[12] != null) {
                vehicleViewDto.setColor(VehicleColor.valueOf(String.valueOf(objects[12])));
            }

            if (objects[13] != null) {
                vehicleViewDto.setDescription(String.valueOf(objects[13]));
            }

            if (objects[14] != null) {
                vehicleViewDto.setImageUrl(String.valueOf(objects[14]));
            }

            if (objects[15] != null) {
                vehicleViewDto.setLocationId(Long.valueOf(String.valueOf(objects[15])));
            }

            if (objects[16] != null) {
                vehicleViewDto.setSectionId(Long.valueOf(String.valueOf(objects[16])));
            }

//            if (objects[17] != null) {
//                vehicleViewDto.setSectionName(String.valueOf(objects[17]));
//            }
//
//            if (objects[18] != null) {
//                vehicleViewDto.setModelName(String.valueOf(objects[18]));
//            }
//
//            if (objects[19] != null) {
//                vehicleViewDto.setModelBrand(String.valueOf(objects[19]));
//            }
//
//            if (objects[20] != null) {
//                vehicleViewDto.setModelType(String.valueOf(objects[20]));
//            }
//
//            if (objects[21] != null) {
//                vehicleViewDto.setModelImage(String.valueOf(objects[21]));
//            }
//
//            if (objects[22] != null) {
//                vehicleViewDto.setLocationName(String.valueOf(objects[22]));
//            }

//            if (objects[23] != null) {
//                vehicleViewDto.setLocationAddress(String.valueOf(objects[23]));
//            }
//
//            if(objects[24] != null){
//                vehicleViewDto.setRentalStatus(RentalStatus.valueOf(String.valueOf(objects[24])));
//            }
//
//            if (objects[25] != null) {
//                vehicleViewDto.setMaintenanceStartDate(DateUtil.localDateTimeToMilliseconds((LocalDateTime) objects[25]));
//            }
//
//            if (objects[26] != null) {
//                vehicleViewDto.setMaintenanceDescription(String.valueOf(objects[26]));
//            }
//
//            if (objects[27] != null) {
//                vehicleViewDto.setMaintenanceCost(Double.valueOf(String.valueOf(objects[27])));
//            }
//
//            if (objects[28] != null) {
//                vehicleViewDto.setMaintenanceDamageType(DamageType.valueOf(String.valueOf(objects[28])));
//            }

            if (objects[29] != null) {
                vehicleViewDto.setCreatedByName(String.valueOf(objects[29]));
            }

            if (objects[30] != null) {
                vehicleViewDto.setCreatedById(Long.valueOf(String.valueOf(objects[30])));
            }

            vehicleViewDtoList.add(vehicleViewDto);

        }
        return vehicleViewDtoList;
    }


    public Long countVehicles(Long tenantId, VehicleFilter vehicleFilter, String memberIdList) {

       String where = filterConditions(tenantId, vehicleFilter, memberIdList);

        String queryString = "Select count(v.id) " +
                " FROM vehicles v " +
                " LEFT JOIN rate_dates rt ON v.rate_dates_id = rt.id " +
                " LEFT JOIN sections s ON v.section_id = s.id " +
                " LEFT JOIN models m ON v.model_id = m.id " +
                " LEFT JOIN locations l ON v.location_id = l.id " +
                " LEFT JOIN rentals r on r.vehicle_id = v.id " +
                " LEFT JOIN maintenance_records mr on mr.vehicle_id = v.id " +
                " WHERE " + where + " and v.deleted_at is null " +
                " GROUP BY v.id ORDER BY v.id DESC ";

        Query query = entityManager.createNativeQuery(queryString);

        List<Object> resultList = query.getResultList();
        return Long.valueOf(resultList.size());
    }
}
