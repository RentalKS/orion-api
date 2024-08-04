package com.orion.repository;

import com.orion.entity.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    @Query("SELECT r FROM Rental r WHERE r.customer.id = :customerId")
    List<Rental> findByCustomerId(@Param("customerId") Long customerId);

    @Query("SELECT r FROM Rental r WHERE r.vehicle.id = :vehicleId")
List<Rental> findByVehicleId(@Param("vehicleId") Long vehicleId);

    @Query("SELECT r FROM Rental r WHERE r.startDate >= :startDate AND r.endDate <= :endDate")
List<Rental> findRentalsWithinDateRange(@Param("startDate") LocalDateTime startDate,
                                        @Param("endDate") LocalDateTime endDate);
}