package com.orion.repository;

import com.orion.entity.Payment;
import com.orion.enums.payment.PaymentStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("SELECT p FROM Payment p WHERE p.rental.id = :rentalId")
    List<Payment> findByRentalId(@Param("rentalId") Long rentalId);

    @Transactional
    @Modifying
    @Query("UPDATE Payment p SET p.status = :status  WHERE p.rental.id = :rentalId")
    void updatePaymentStatusByRentalId(@Param("rentalId") Long rentalId, @Param("status") PaymentStatus status, @Param("signature") String signature);

    @Query("SELECT p FROM Payment p WHERE p.transactionId = :transactionId")
    Optional<Payment> findByTransactionId(String transactionId);
}