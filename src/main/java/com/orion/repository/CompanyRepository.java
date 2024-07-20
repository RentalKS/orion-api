package com.orion.repository;

import com.orion.dto.CompanyDto;
import com.orion.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    @Query("select new com.orion.dto.CompanyDto(c.createdAt, c.createdBy, c.id, c.companyName, c.companyAddress, c.companyEmail, c.companyPhone, c.companyLogo, c.zipCode, c.city, c.state,c.user.id) " +
            "from Company c where c.id = :companyId and c.user.email = :email and c.deletedAt is null")
    Optional<CompanyDto> findCompany(@Param("companyId") Long companyId,@Param("email") String email);

    @Query("select c from Company c where c.id = :companyId and c.deletedAt is null")
    Optional<Company> findCompanyById(@Param("companyId") Long companyId);

    @Query("select new com.orion.dto.CompanyDto(c.createdAt, c.createdBy, c.id, c.companyName, c.companyAddress, c.companyEmail, c.companyPhone, c.companyLogo, c.zipCode, c.city, c.state,c.user.id) " +
            "from Company c where c.deletedAt is null and c.user.email = :email")
    Optional<CompanyDto> findAllCompanies(@Param("email") String email);


}
