package com.orion.repository;

import com.orion.dto.customer.CustomerDto;
import com.orion.entity.Customer;
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

    @Query("Select new com.orion.dto.customer.CustomerDto(c.id, c.name, c.email, c.phoneNumber, c.licenseNumber) " +
            "FROM Customer c WHERE c.id = :customerId AND c.tenant.id = :id and c.deletedAt is null")
    Optional<CustomerDto> findCustomerByIdFromDto(Long customerId, Long id);
}