package com.orion.repository;

import com.orion.dto.vehicle.VehicleDto;
import com.orion.entity.Vehicle;
import com.orion.enums.vehicle.VehicleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    @Query("Select new com.orion.dto.vehicle.VehicleDto(v.id, v.registrationNumber, v.model.id, v.year, v.fuelType, " +
            " v.mileage, v.transmission, v.color, v.description, v.image, v.location.id, v.rateDates.id, v.rateDates.name,v.rateDates.dailyRate,v.rateDates.weeklyRate,v.rateDates.monthlyRate ) " +
            "FROM Vehicle v WHERE v.id = :vehicleId AND v.tenant.id = :tenantId and v.deletedAt is null and v.deactivatedAt is null")
    Optional<VehicleDto> findVehicleByIdFromDto(@Param("vehicleId") Long vehicleId, @Param("tenantId") Long tenantId);

    @Query("SELECT new com.orion.dto.vehicle.VehicleDto(v.id, v.registrationNumber, v.model.id, v.year, v.fuelType, " +
            " v.mileage, v.transmission, v.color, v.description, v.image, v.location.id, v.rateDates.id, v.rateDates.name,v.rateDates.dailyRate,v.rateDates.weeklyRate,v.rateDates.monthlyRate ) " +
            "FROM Vehicle v WHERE v.tenant.id = :tenantId and v.deletedAt is null and v.deactivatedAt is null")
    List<VehicleDto> findAllVehicles(@Param("tenantId") Long tenantId);
}