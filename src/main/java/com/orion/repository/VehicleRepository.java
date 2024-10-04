package com.orion.repository;

import com.orion.dto.insurancePolicy.InsurancePolicyDto;
import com.orion.dto.maintenanceRecord.MaintenanceRecordDto;
import com.orion.dto.model.ModelDto;
import com.orion.dto.rates.RatesDto;
import com.orion.dto.section.SectionDto;
import com.orion.dto.vehicle.VehicleViewDto;
import com.orion.entity.Vehicle;
import com.orion.enums.vehicle.DamageType;
import com.orion.enums.vehicle.RentalStatus;
import com.orion.util.DateUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    @Query("Select new com.orion.dto.vehicle.VehicleViewDto(v.id, v.registrationNumber, v.model.id, v.year, v.fuelType, " +
            " v.mileage, v.transmission, v.color, v.description, v.image, v.location.id, v.rateDates.id, v.rateDates.name,v.rateDates.dailyRate,v.rateDates.weeklyRate,v.rateDates.monthlyRate,v.insurancePolicy.id,v.section.id ) " +
            "FROM Vehicle v WHERE v.id = :vehicleId AND v.tenant.id = :tenantId and v.deletedAt is null and v.deactivatedAt is null")
    Optional<VehicleViewDto> findVehicleByIdFromDto(@Param("vehicleId") Long vehicleId, @Param("tenantId") Long tenantId);

    @Query("SELECT new com.orion.dto.vehicle.VehicleViewDto(v.id, v.registrationNumber, v.model.id, v.year, v.fuelType, " +
            " v.mileage, v.transmission, v.color, v.description, v.image, v.location.id, v.rateDates.id, v.rateDates.name,v.rateDates.dailyRate,v.rateDates.weeklyRate,v.rateDates.monthlyRate,v.insurancePolicy.id,v.section.id ) " +
            "FROM Vehicle v " +
            "WHERE v.tenant.id = :tenantId and v.createdBy = :email " +
            "and v.deletedAt is null and v.deactivatedAt is null")
    Page<VehicleViewDto> findAllVehicles(@Param("tenantId") Long tenantId, @Param("email") String email, Pageable pageable);

    @Query("SELECT v from Vehicle v where v.id = :vehicleId and v.tenant.id = :tenantId and v.deletedAt is null and v.deactivatedAt is null")
    Optional<Vehicle> findVehicleById(@Param("vehicleId") Long vehicleId, @Param("tenantId") Long tenantId);
    @Query("SELECT new com.orion.dto.vehicle.VehicleViewDto(" +
            "v.createdAt, v.id, " +
            "v.model.id, v.model.name, v.model.brand.id, v.model.type, " +
            "v.model.seatingCapacity, v.model.fuelEfficiency, v.model.modelImage, " +
            "v.rateDates.id, v.rateDates.name, v.rateDates.dailyRate, v.rateDates.weeklyRate, v.rateDates.monthlyRate, " +
            "v.insurancePolicy.id, v.insurancePolicy.policyNumber, v.insurancePolicy.providerName, " +
            "v.insurancePolicy.coverageDetails, v.insurancePolicy.vehicle.id, " +
            "v.section.id, v.section.createdAt, v.section.sectionName, v.section.sectionDescription, v.section.sectionImage, " +
            "v.section.category.id, " +
            "v.user.firstname, v.user.id " +
            ") " +
            "FROM Vehicle v " +
            "WHERE v.tenant.id = :tenantId and v.id = :vehicleId AND v.deletedAt IS NULL AND v.deactivatedAt IS NULL ")
    Optional<VehicleViewDto> findVehicleDto(@Param("vehicleId") Long vehicleId,@Param("tenantId") Long tenantId);

    @Query("SELECT count(m.id) > 0 from MaintenanceRecord m " +
            "where m.vehicle.id = :vehicleId " +
            "and m.maintenanceStartDate <= :endDate and m.maintenanceEndDate >= :startDate and m.damageType is not null")
    Boolean isVehicleOnMaintenance(@Param("vehicleId") Long vehicleId,@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}