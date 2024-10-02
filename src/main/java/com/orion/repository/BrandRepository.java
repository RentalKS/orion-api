package com.orion.repository;

import com.orion.dto.brand.BrandDto;
import com.orion.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand,Long> {
    @Query("SELECT new com.orion.dto.brand.BrandDto(b.id,b.name,b.logo,b.description) from Brand b where b.id = :brandId and b.tenant.id = :tenantId")
    Optional<BrandDto> findBrandByIdAndByTenant(@Param("brandId") Long brandId, @Param("tenantId") Long tenantId);

    @Query("SELECT b from Brand b where b.id = :id and b.tenant.id = :tenantId")
    Optional<Brand> findById(@Param("id") Long id, @Param("tenantId") Long tenantId);

    @Query("SELECT new com.orion.dto.brand.BrandDto(b.id,b.name,b.logo,b.description) from Brand b where b.tenant.id = ?1 and b.createdBy = ?2")
    List<BrandDto> findAllBrandsByTenant(Long tenantId, String currentEmail);
}
