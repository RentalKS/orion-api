package com.orion.controller;

import com.orion.dto.chatbot.ChatbotRequest;
import com.orion.dto.chatbot.ChatbotResponse;
import com.orion.service.ChatbotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/chatbot")
@RequiredArgsConstructor
public class ChatbotController {
    private final ChatbotService chatbotService;

    @PostMapping("/ask")
    public ResponseEntity<ChatbotResponse> handleChatbotRequest(@RequestBody ChatbotRequest request) {
        ChatbotResponse response = chatbotService.processRequest(request);
        return ResponseEntity.ok(response);
    }
}
