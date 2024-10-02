package com.orion.controller;
import com.orion.entity.Notification;
import com.orion.security.CustomUserDetails;
import com.orion.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<Notification>> getUserNotifications(@AuthenticationPrincipal CustomUserDetails userDetails) {
        String email = userDetails.getUsername();
        List<Notification> notifications = notificationService.getUserNotifications(email);
        return ResponseEntity.ok(notifications);
    }

    @PostMapping("/read/{id}")
    public ResponseEntity<Void> markNotificationAsRead(@PathVariable Long id) {
        notificationService.markNotificationAsRead(id);
        return ResponseEntity.ok().build();
    }
}