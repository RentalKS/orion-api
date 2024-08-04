package com.orion.repository;

import com.orion.dto.location.LocationDto;
import com.orion.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {
    @Query("SELECT l FROM Location l WHERE l.id = :locationId and l.deletedAt is null")
    Optional<Location> findLocationById(@Param("locationId") Long locationId);

    @Query("Select new com.orion.dto.location.LocationDto(l.id, l.createdAt, l.address, l.city, l.state, l.zipCode, l.country,l.tables) " +
            "FROM Location l WHERE l.tenant.id = :tenantId and l.deletedAt is null ")
    List<LocationDto> findAllLocationsByTenant(@Param("tenantId") Long tenantId);

    @Query("Select new com.orion.dto.location.LocationDto(l.id, l.createdAt, l.address, l.city, l.state, l.zipCode, l.country,l.tables) " +
            "FROM Location l WHERE l.id = :locationId and l.tenant.id = :tenantId and l.deletedAt is null ")
    Optional<LocationDto> findLocationByIdAndByTenant(@Param("locationId") Long locationId, @Param("tenantId") Long tenantId);
}