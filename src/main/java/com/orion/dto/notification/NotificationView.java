package com.orion.dto.notification;

import com.orion.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationView {
    private Long id;
    private String title;
    private String message;
    private boolean read;
    private Long createdAt;

    public NotificationView(Long id, String title, String message, boolean read, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.read = read;
        this.createdAt = DateUtil.localDateTimeToMilliseconds(createdAt);
    }
}
