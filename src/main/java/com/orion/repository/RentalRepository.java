package com.orion.repository;

import com.orion.dto.rental.RentalDto;
import com.orion.entity.Booking;
import com.orion.entity.Rental;
import com.orion.enums.vehicle.RentalStatus;
import com.orion.enums.vehicle.VehicleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {
    @Query("SELECT r FROM Rental r WHERE r.customer.id = :customerId")
    List<Rental> findByCustomerId(@Param("customerId") Long customerId);

    @Query("SELECT r FROM Rental r WHERE r.vehicle.id = :vehicleId")
List<Rental> findByVehicleId(@Param("vehicleId") Long vehicleId);

    @Query("SELECT r FROM Rental r WHERE r.startDate >= :startDate AND r.endDate <= :endDate")
List<Rental> findRentalsWithinDateRange(@Param("startDate") LocalDateTime startDate,
                                        @Param("endDate") LocalDateTime endDate);
    @Query("SELECT r FROM Rental r WHERE r.status = 'WAITING_PAYMENT' AND r.id = :rentalId and r.deletedAt is null")
    Optional<Rental> findRentalWaitingToPayment(Long rentalId);

    @Query(" SELECT sum(r.totalCost) from Rental r left join Booking b on r.booking.id = b.id where b in :bookings and r.deletedAt is null and b.deletedAt is null")
    Double findTotalRevenue(List<Booking> bookings);

    @Query("SELECT SUM(r.totalCost) FROM Rental r WHERE r.booking.id = :bookingId")
    Double findTotalRevenueByBookingId(@Param("bookingId") Long bookingId);

    @Query("Select r from Rental r where r.booking.id = :bookingId and r.deletedAt is null")
    Optional<Rental> findByBooking(@Param("bookingId") Long bookingId);

    @Query("Select r from Rental r where r.status = :rentalStatus and r.vehicleStatus = :vehicleStatus and r.deletedAt is null")
    List<Rental> findRentalsWaitingToStart(@Param("rentalStatus") RentalStatus rentalStatus,@Param("vehicleStatus") VehicleStatus vehicleStatus);

    @Query("SELECT new com.orion.dto.rental.RentalDto(r.id, r.startDate, r.endDate, r.status, r.totalCost, r.vehicle.id, " +
            "c.id, c.name, c.lastName, c.email, c.phoneNumber, c.licenseNumber, c.createdBy,r.vehicleStatus) " +
            "FROM Rental r JOIN r.customer c " +
            "WHERE r.booking.id = :bookingId " +
            "AND r.deletedAt IS NULL AND r.booking.deletedAt IS NULL")
    Optional<RentalDto> findByBookingDto(@Param("bookingId") Long bookingId);

    @Query("SELECT r FROM Rental r WHERE r.id = :rentalId AND r.tenant.id = :tenantId")
    Optional<Rental> findById(@Param("rentalId") Long rentalId, @Param("tenantId") Long tenantId);

    @Query("SELECT new com.orion.dto.rental.RentalDto(r.id, r.startDate, r.endDate, r.status, r.totalCost, r.vehicle.id,r.vehicleStatus) " +
            "FROM Rental r JOIN r.customer c " +
            "WHERE c.id = :customerId " +
            "AND r.deletedAt IS NULL AND r.customer.deletedAt IS NULL")
    List<RentalDto> findCustomerRentals(@Param("customerId") Long customerId);
}