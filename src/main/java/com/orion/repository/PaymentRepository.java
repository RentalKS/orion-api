package com.orion.repository;

import com.orion.entity.Payment;
import com.orion.enums.payment.PaymentStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("SELECT p FROM Payment p WHERE p.rental.id = :rentalId and p.deletedAt is null")
    List<Payment> findByRentalId(@Param("rentalId") Long rentalId);

    @Query("SELECT p FROM Payment p WHERE p.transactionId = :transactionId and p.deletedAt is null")
    Optional<Payment> findByTransactionId(String transactionId);

    @Query("SELECT p.signature FROM Payment p WHERE p.transactionId = :transactionId and p.deletedAt is null")
    Optional<String> findSignatureByTransactionId(String transactionId);

    @Query("SELECT p.signature FROM Payment p WHERE p.rental.id = :rentalId and p.deletedAt is null ")
    List<String> findSignatureByRentalId(Long rentalId);
}