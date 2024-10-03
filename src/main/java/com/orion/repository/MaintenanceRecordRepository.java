package com.orion.repository;

import com.orion.dto.maintenanceRecord.MaintenanceRecordDto;
import com.orion.entity.MaintenanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MaintenanceRecordRepository extends JpaRepository<MaintenanceRecord, Long> {
    @Query("SELECT new com.orion.dto.maintenanceRecord.MaintenanceRecordDto(m.id,m.maintenanceStartDate,m.maintenanceEndDate,m.description,m.cost,m.damageType,m.vehicle.id)  " +
            "FROM MaintenanceRecord m WHERE m.id = :id and m.deletedAt is null and m.tenant.id = :tenantId ")
    Optional<MaintenanceRecordDto> findByIdDto(@Param("id") Long id, @Param("tenantId") Long tenantId);

    @Query("SELECT new com.orion.dto.maintenanceRecord.MaintenanceRecordDto(m.id,m.maintenanceStartDate,m.maintenanceEndDate,m.description,m.cost,m.damageType,m.vehicle.id)  " +
            "FROM MaintenanceRecord m WHERE  m.deletedAt is null and m.createdBy = :email and m.tenant.id = :tenantId ")
    List<MaintenanceRecordDto> findAllByTenant(@Param("email") String email, @Param("tenantId") Long tenantId);

    @Query("Select m from MaintenanceRecord m where m.id = :id and m.deletedAt is null and m.tenant.id = :tenantId")
    Optional<MaintenanceRecord> findById(@Param("id") Long id, @Param("tenantId") Long tenantId);

}