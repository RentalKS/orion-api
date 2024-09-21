package com.orion.repository;

import com.orion.entity.Booking;
import com.orion.entity.Rental;
import com.orion.enums.vehicle.RentalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b WHERE b.customer.id = :customerId")
    List<Booking> findByCustomerId(@Param("customerId") Long customerId);

    @Query("SELECT b FROM Booking b WHERE b.vehicle.id = :vehicleId")
    List<Booking> findByVehicleId(@Param("vehicleId") Long vehicleId);

    @Query("SELECT b FROM Booking b WHERE b.status = 'PENDING' AND " +
            "(b.startDate <= :endDate AND b.endDate >= :startDate)")
    List<Booking> findActiveBookings(@Param("startDate") LocalDateTime startDate,
                                     @Param("endDate") LocalDateTime endDate);

    @Query("Select count(b.id) > 0 from Booking b " +
            " where b.vehicle.id = :vehicleId " +
            " and b.status = 'RENTED' " +
            " and b.bookingStatus = 'ONGOING' and " +
            "(b.startDate <= :endDate AND b.endDate >= :startDate)")
    boolean findBookingsByVehicleAndDateRange(Long vehicleId, LocalDateTime startDate, LocalDateTime endDate);

    @Query("Select b from Booking b where b.status = :status ")
    List<Booking> findByStatus(@Param("status") RentalStatus status);

    @Query("Select b from Booking b where b.status = 'PENDING' and b.bookingStatus = 'RESERVED' and " +
            "(b.startDate <= :endDate AND b.endDate >= :startDate)")
    List<Booking> findBookingsBetweenDates(LocalDateTime startDate, LocalDateTime endDate);
}