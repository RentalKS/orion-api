package com.orion.repository;

import com.orion.dto.category.CategoryDto;
import com.orion.dto.company.CompanyDto;
import com.orion.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    @Query("select new com.orion.dto.company.CompanyDto(c.createdAt, c.createdBy, c.id, c.companyName, c.companyAddress, c.companyEmail, c.companyPhone, c.companyLogo, c.zipCode, c.city, c.state,c.user.id) " +
            "from Company c where c.id = :companyId and c.deletedAt is null and c.deactivatedAt is null and c.tenant.id = :tenantId")
    Optional<CompanyDto> findCompany(@Param("companyId") Long companyId,@Param("tenantId") Long tenantId);

    @Query("select c from Company c where c.id = :companyId and c.deletedAt is null and c.tenant.id = :tenantId")
    Optional<Company> findCompanyById(@Param("companyId") Long companyId, @Param("tenantId") Long tenantId);

    @Query("select new com.orion.dto.company.CompanyDto(c.createdAt, c.createdBy, c.id, c.companyName, c.companyAddress, c.companyEmail, c.companyPhone, c.companyLogo, c.zipCode, c.city, c.state,c.user.id) " +
            "from Company c where c.deletedAt is null and c.user.email = :email and c.tenant.id = :tenantId")
    List<CompanyDto> findAllCompanies(@Param("email") String email,@Param("tenantId") Long tenantId);

    @Query("select new com.orion.dto.category.CategoryDto(c.id, c.createdAt, c.categoryName, c.categoryDescription, c.company.id) " +
            "from Category c where c.company.id = :companyId and c.deletedAt is null and c.tenant.id = :tenantId")
    List<CategoryDto> findCompanyCategories(@Param("companyId") Long companyId, @Param("tenantId") Long tenantId);
}
