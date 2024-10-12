package com.orion.service.notification;

import com.orion.entity.Notification;
import com.orion.entity.User;

import com.orion.exception.InternalException;
import com.orion.repository.NotificationRepository;
import com.orion.repository.UserRepository;
import com.orion.service.user.UserService;
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
    private final NotificationRepository repository;
    private final UserService userService;
    private final SimpMessagingTemplate messagingTemplate;

    public void sendNotification(String email, String title, String message) {
        try {
            User user = userService.findByEmail(email);
            Notification notification = new Notification();
            notification.setTitle(title);
            notification.setMessage(message);
            notification.setTimestamp(LocalDateTime.now());
            notification.setRead(false);
            notification.setUser(user);

            this.save(notification);

            messagingTemplate.convertAndSendToUser(user.getEmail(),NOTIFICATION_DESTINATION, notification);
        } catch (Exception e) {
            log.error("Failed to send notification: {}", e.getMessage());
            throw new InternalException(e.getLocalizedMessage(), e.getCause());
        }
    }
    private Notification save(Notification notification) {
        try {
            return this.repository.save(notification);
        } catch (Exception e) {
            log.error("Failed to save notification. Error: {}", e.getMessage());
            throw new RuntimeException("Failed to save notification",e.getCause());
        }
    }

    public List<Notification> getUserNotifications(String email) {
        return repository.findByUserId(email);
    }

    public void markNotificationAsRead(Long notificationId) {
        Notification notification = repository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(true);
        repository.save(notification);
    }
}