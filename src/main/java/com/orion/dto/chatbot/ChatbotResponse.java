package com.orion.dto.chatbot;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChatbotResponse {
    private String responseText;
    private boolean success;
}
