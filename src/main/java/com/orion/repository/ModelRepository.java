package com.orion.repository;

import com.orion.dto.model.ModelDto;
import com.orion.entity.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ModelRepository extends JpaRepository<Model, Long> {
   @Query("SELECT new com.orion.dto.model.ModelDto(m.id, m.name, m.brand, m.type, m.seatingCapacity, m.fuelEfficiency) " +
           "FROM Model m WHERE m.id = :modelId AND m.tenant.id = :tenantId")
   Optional<ModelDto> findModelByIdFromDto(@Param("modelId") Long modelId, @Param("tenantId") Long tenantId);

    @Query("SELECT new com.orion.dto.model.ModelDto(m.id, m.name, m.brand, m.type, m.seatingCapacity, m.fuelEfficiency) " +
           "FROM Model m WHERE m.tenant.id = :tenantId")
    List<ModelDto> findAllModelsByTenantId(@Param("tenantId") Long tenantId);

    @Query("SELECT m FROM Model m WHERE m.id = :modelId and m.tenant.id = :tenantId")
    Optional<Model> findModelById(@Param("modelId") Long modelId, @Param("tenantId") Long tenantId);
}