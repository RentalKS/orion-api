package com.orion.repository;

import com.orion.dto.location.LocationDto;
import com.orion.entity.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    @Query("SELECT l FROM Location l WHERE l.id = :locationId and l.tenant.id = :tenantId and l.deletedAt is null")
    Optional<Location> findLocationById(@Param("locationId") Long locationId, @Param("tenantId") Long tenantId);

    @Query("Select new com.orion.dto.location.LocationDto(l.id, l.createdAt, l.address, l.city, l.state, l.zipCode, l.country,l.tables) " +
            "FROM Location l WHERE l.tenant.id = :tenantId and l.createdBy = :email and l.deletedAt is null ")
    Page<LocationDto> findAllLocationsByTenant(@Param("tenantId") Long tenantId, @Param("email") String email, Pageable pageable);

    @Query("Select new com.orion.dto.location.LocationDto(l.id, l.createdAt, l.address, l.city, l.state, l.zipCode, l.country,l.tables) " +
            "FROM Location l WHERE l.id = :locationId and l.tenant.id = :tenantId and l.deletedAt is null ")
    Optional<LocationDto> findLocationByIdAndByTenant(@Param("locationId") Long locationId, @Param("tenantId") Long tenantId);
}