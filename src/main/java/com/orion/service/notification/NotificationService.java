package com.orion.service.notification;

import com.orion.entity.Notification;
import com.orion.entity.User;

import com.orion.repository.NotificationRepository;
import com.orion.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class NotificationService {

    private static final String NOTIFICATION_DESTINATION = "/user/queue/notifications";
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public void sendNotification(Long userId, String title, String message) {
        try {

            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
            Notification notification = new Notification();
            notification.setTitle(title);
            notification.setMessage(message);
            notification.setTimestamp(LocalDateTime.now());
            notification.setRead(false);
            notification.setUser(user);

            notificationRepository.save(notification);

            messagingTemplate.convertAndSendToUser(user.getEmail(),NOTIFICATION_DESTINATION, notification);
        } catch (RuntimeException e) {
            log.error("Failed to send notification: {}", e.getMessage());
        }
    }

    public List<Notification> getUserNotifications(String email) {
        return notificationRepository.findByUserId(email);
    }

    public void markNotificationAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(true);
        notificationRepository.save(notification);
    }
}