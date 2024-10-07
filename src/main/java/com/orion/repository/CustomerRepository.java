package com.orion.repository;

import com.orion.dto.customer.CustomerDto;
import com.orion.entity.Customer;
import com.orion.entity.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query("SELECT c FROM Customer c WHERE c.email = :email")
    Optional<Customer> findByEmail(@Param("email") String email);

    @Query("SELECT DISTINCT c FROM Customer c JOIN c.rentals r WHERE r.status = 'ONGOING'")
    List<Customer> findCustomersWithActiveRentals();

    @Query("Select new com.orion.dto.customer.CustomerDto(c.id, c.name,c.lastName, c.email, c.phoneNumber, c.licenseNumber,c.createdBy) " +
            "FROM Customer c WHERE c.id = :customerId AND c.tenant.id = :id and c.deletedAt is null")
    Optional<CustomerDto> findCustomerByIdFromDto(Long customerId, Long id);

    @Query("SELECT c " +
            "FROM Customer c WHERE  c.id = :customerId and c.tenant.id = :tenantId and c.deletedAt is null")
    Optional<Customer> findCustomerByIdAndTenantId(@Param("customerId") Long customerId, @Param("tenantId") Long tenantId);

    @Query("SELECT r FROM Rental r WHERE r.customer.id = :customerId and r.deletedAt is null and r.customer.deletedAt is null and r.vehicle.deletedAt is null")
    List<Rental> findCustomerRentals(@Param("customerId") Long customerId);

    @Query("SELECT new com.orion.dto.customer.CustomerDto(c.id, c.name,c.lastName, c.email, c.phoneNumber, c.licenseNumber,c.createdBy) " +
            "FROM Customer c WHERE c.tenant.id = :tenantId and c.createdBy = :email and c.deletedAt is null")
    List<CustomerDto> findAllCustomersFromDto(@Param("tenantId") Long tenantId,@Param("email") String email);

    @Query("SELECT c.id FROM Customer c WHERE c.createdBy IN :agencyEmails and c.deletedAt is null")
    List<Long> findCustomerIdsFromAgencies(@Param("agencyEmails") List<String> agencyEmails);
}