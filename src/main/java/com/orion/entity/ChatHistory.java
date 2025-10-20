package com.orion.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "chat_history")
public class ChatHistory extends BaseEntity {

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "query", nullable = false)
    private String query;

    @Column(name = "response", nullable = false)
    private String response;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;
}
