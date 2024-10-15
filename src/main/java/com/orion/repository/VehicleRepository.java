package com.orion.repository;

import com.orion.dto.maintenanceRecord.MaintenanceRecordDto;
import com.orion.dto.vehicle.VehicleDto;
import com.orion.dto.vehicle.VehicleViewDto;
import com.orion.entity.Vehicle;
import com.orion.enums.vehicle.VehicleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    @Query("SELECT new com.orion.dto.vehicle.VehicleViewDto(" +
            "v.id, v.registrationNumber, v.year, v.fuelType, " +
            "v.mileage, v.transmission, v.color, v.description, v.image, " +
            "v.location.id,v.rateDates.id,v.insurancePolicy.id,v.section.id,r.vehicleStatus," +
            "m.id,m.name,m.brand.id,m.type,m.seatingCapacity,m.fuelEfficiency,m.modelImage ) " +
            "FROM Vehicle v " +
            "LEFT JOIN Rental r on r.vehicle.id = v.id " +
            "LEFT JOIN v.section s " +
            "LEFT JOIN Category c on s.category.id = c.id " +
            "left join c.company cc " +
            "left join v.model m " +
            "WHERE v.tenant.id = :tenantId " +
            "and v.user.id in :userIds " +
            "AND (:from is null or v.createdAt >= :from ) " +
            "AND (:to is null or v.createdAt <= :to ) " +
            "AND (:status is null or r.vehicleStatus = :status ) " +
            "AND (:locationId is null or v.location.id = :locationId ) " +
            "AND (:categoryId is null or c.id = :categoryId ) " +
            "AND (:sectionId is null or s.id = :sectionId ) " +
            "AND (:companyId is null or cc.id = :companyId ) " +
            "and v.deletedAt is null and v.deactivatedAt is null " +
            "AND (:searchTerm IS NULL OR " +
            "LOWER(v.registrationNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%'))" +
            "OR LOWER(v.location.tables) LIKE LOWER(CONCAT('%', :searchTerm, '%'))" +
            "OR LOWER(r.vehicleStatus) LIKE LOWER(CONCAT('%', :searchTerm, '%')) )")
    Page<VehicleViewDto> findAllVehicles(@Param("tenantId") Long tenantId,@Param("userIds") List<Long> userIds,
                                         @Param("from") LocalDateTime from, @Param("to") LocalDateTime to,
                                         @Param("locationId") Long locationId, @Param("companyId") Long companyId,
                                         @Param("categoryId") Long categoryId, @Param("sectionId") Long sectionId,
                                         @Param("status") VehicleStatus status, @Param("searchTerm") String searchTerm, Pageable pageable);


    @Query("SELECT v from Vehicle v where v.id = :vehicleId and v.tenant.id = :tenantId and v.deletedAt is null and v.deactivatedAt is null")
    Optional<Vehicle> findVehicleById(@Param("vehicleId") Long vehicleId, @Param("tenantId") Long tenantId);
    @Query("SELECT new com.orion.dto.vehicle.VehicleViewDto(" +
            "v.createdAt," +
            "v.id, " +
            "m.id," +
            "m.name," +
            "m.brand.id," +
            "m.type, " +
            "m.seatingCapacity, " +
            "m.fuelEfficiency, " +
            "m.modelImage, " +
            "rd.id, " +
            "rd.name, " +
            "rd.dailyRate, " +
            "rd.weeklyRate, " +
            "rd.monthlyRate, " +
            "i.id, " +
            "i.policyNumber, " +
            "i.providerName, " +
            "i.coverageDetails, " +
            "i.vehicle.id, " +
            "s.id, " +
            "s.createdAt, " +
            "s.sectionName, " +
            "s.sectionDescription, " +
            "s.sectionImage, " +
            "s.category.id, " +
            "u.firstname, " +
            "u.id," +
            "c.id," +
            "c.createdAt," +
            "c.categoryName," +
            "c.categoryDescription," +
            "c.company.id, " +
            "l.id," +
            "l.createdAt," +
            "l.address," +
            "l.city," +
            "l.state," +
            "l.zipCode," +
            "l.country," +
            "l.tables" +
            ") " +
            "FROM Vehicle v " +
            "left join v.model m " +
            "left join v.rateDates rd " +
            "left join v.section.category c " +
            "left join v.insurancePolicy i " +
            "left join v.location l " +
            "left join v.section s " +
            "left join v.user u " +
            "WHERE v.tenant.id = :tenantId " +
            "and v.id = :vehicleId " +
            "and v.deletedAt IS NULL " +
            "AND v.deactivatedAt IS NULL " +
            "and u.deletedAt is null ")
    Optional<VehicleViewDto> findVehicleDto(@Param("vehicleId") Long vehicleId,@Param("tenantId") Long tenantId);

    @Query("SELECT count(m.id) > 0 from MaintenanceRecord m " +
            "where m.vehicle.id = :vehicleId " +
            "and m.maintenanceStartDate <= :endDate and m.maintenanceEndDate >= :startDate and m.damageType is not null")
    Boolean isVehicleOnMaintenance(@Param("vehicleId") Long vehicleId,@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
            @Query("select new com.orion.dto.maintenanceRecord.MaintenanceRecordDto(" +
            "m.id, " +
            "m.maintenanceStartDate, " +
            "m.maintenanceEndDate, " +
            "m.description, " +
            "m.cost, " +
            "m.damageType, " +
            "m.vehicle.id) " +
            "from MaintenanceRecord m " +
            "where m.vehicle.id = :vehicleId")
    List<MaintenanceRecordDto> findMaintenanceRecord(Long vehicleId);


    @Query("SELECT new com.orion.dto.vehicle.VehicleDto(v.id,v.location.id,v.rateDates.id,v.section.id,v.registrationNumber,v.year,v.fuelType,v.mileage,v.transmission,v.color,v.description,v.image)" +
            " from Vehicle v where v.model.id = :modelId and v.tenant.id = :tenantId and v.deletedAt is null and v.deactivatedAt is null")
    List<VehicleDto> findVehiclesFromModel(@Param("modelId") Long modelId, @Param("tenantId") Long tenantId);

    @Query("SELECT new com.orion.dto.vehicle.VehicleDto(v.id,v.location.id,v.rateDates.id,v.section.id,v.registrationNumber,v.year,v.fuelType,v.mileage,v.transmission,v.color,v.description,v.image)" +
            " from Vehicle v where v.section.id = :sectionId and v.tenant.id = :tenantId and v.deletedAt is null and v.deactivatedAt is null")
    List<VehicleDto> findVehiclesFromSection(@Param("sectionId") Long sectionId, @Param("tenantId") Long tenantId);
}