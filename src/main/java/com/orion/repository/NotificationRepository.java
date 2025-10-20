package com.orion.repository;

import com.orion.dto.notification.NotificationView;
import com.orion.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("SELECT new com.orion.dto.notification.NotificationView(n.id,n.title,n.message,n.read,n.createdAt) FROM Notification n WHERE n.user.email = :email and n.deletedAt is null")
    List<NotificationView> findByUserId(@Param("email") String email);

    @Query("SELECT n FROM Notification n WHERE n.user.email = :email and n.deletedAt is null")
    List<Notification> findByEmailId(@Param("email") String email);
}
