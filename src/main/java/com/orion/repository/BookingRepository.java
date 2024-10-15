package com.orion.repository;
import com.orion.dto.booking.BookingViewDto;
import com.orion.entity.Booking;
import com.orion.enums.vehicle.RentalStatus;
import com.orion.enums.vehicle.VehicleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b WHERE b.customer.id = :customerId")
    List<Booking> findByCustomerId(@Param("customerId") Long customerId);

    @Query("SELECT b FROM Booking b WHERE b.vehicle.id = :vehicleId")
    List<Booking> findByVehicleId(@Param("vehicleId") Long vehicleId);
    @Query("Select count(b.id) > 0 from Booking b " +
            " where b.vehicle.id = :vehicleId and b.deletedAt is null " +
            " AND (b.startDate <= :endDate AND b.endDate >= :startDate)")
    Boolean findBookingsByVehicleAndDateRange(Long vehicleId, LocalDateTime startDate, LocalDateTime endDate);

    @Query("Select b from Booking b " +
            "LEFT join Rental r on r.booking.id = b.id " +
            " where b.createdBy in :email " +
            "and (:rentalStatus is null or r.status = :rentalStatus) " +
            "and (:vehicleStatus is null or r.vehicleStatus = :vehicleStatus) " +
            "and (b.startDate <= :endDate AND b.endDate >= :startDate) " +
            "and b.deletedAt is null")
    List<Booking> findBookingsBetweenDates(@Param("startDate") LocalDateTime startDate,
                                           @Param("endDate") LocalDateTime endDate,
                                           @Param("rentalStatus") RentalStatus rentalStatus,
                                           @Param("vehicleStatus") VehicleStatus vehicleStatus,
                                           @Param("email") List<String> email);

    @Query("SELECT new com.orion.dto.booking.BookingViewDto(b.id,b.startDate,b.endDate,r.vehicleStatus,r.status,b.vehicle.id,b.customer.id) " +
            "FROM Booking b left join Rental r " +
            "WHERE b.deletedAt is null " +
            "and  b.createdBy = :currentEmail " +
            "AND (:startDate IS NULL OR b.startDate >= :startDate) " +
            "AND (:endDate IS NULL OR b.endDate <= :endDate) " +
            "AND (:status IS NULL OR r.status = :status) " +
            "AND (:vehicleStatus IS NULL OR r.vehicleStatus = :vehicleStatus) " +
            "AND (:vehicleId IS NULL OR b.vehicle.id = :vehicleId) " +
            "AND (:customerId IS NULL OR b.customer.id = :customerId) " +
            "AND (:searchTerm IS NULL OR " +
            "LOWER(b.customer.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))" +
            "OR LOWER(b.vehicle.model.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))" +
            "OR LOWER(b.customer.licenseNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            ")")
    Page<BookingViewDto> findBookingsByCustomer(String currentEmail, LocalDateTime startDate, LocalDateTime endDate,
                                                RentalStatus status, VehicleStatus vehicleStatus, String vehicleId,
                                                String customerId, String searchTerm, Pageable pageable);
    @Query("SELECT new com.orion.dto.booking.BookingViewDto(b.id,b.startDate,b.endDate,b.vehicle.id,c.id,c.name,c.lastName,c.email,c.phoneNumber,c.licenseNumber,c.createdBy) " +
            "FROM Booking  b  left join b.customer c " +
            "where b.id = :bookingId and b.deletedAt is null ")
    Optional<BookingViewDto> findBookingDto(@Param("bookingId") Long bookingId);
}