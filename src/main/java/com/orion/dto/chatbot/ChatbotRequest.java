package com.orion.dto.chatbot;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class ChatbotRequest  {
    private Long userId;
    private String query;
}
