package com.aisupport.api.controller;

import com.aisupport.api.dto.*;
import com.aisupport.api.model.ChatSession;
import com.aisupport.api.service.ChatService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@Validated
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping("/start")
    public ResponseEntity<Map<String, Object>> startSession(@Valid @RequestBody SessionStartDTO request) {
        ChatSession session = chatService.startSession(request);

        Map<String, Object> response = new HashMap<>();
        response.put("sessionId", session.getId());
        response.put("customerSessionId", session.getCustomer().getSessionId());
        response.put("status", session.getStatus());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{sessionId}/message")
    public ResponseEntity<ChatResponseDTO> sendMessage(
            @PathVariable Long sessionId,
            @Valid @RequestBody ChatRequestDTO request) {

        ChatResponseDTO response = chatService.sendMessage(
                sessionId, request.getCustomerSessionId(), request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{sessionId}/end")
    public ResponseEntity<Map<String, String>> endSession(
            @PathVariable Long sessionId,
            @RequestParam @NotBlank String customerSessionId) {

        chatService.endSession(sessionId, customerSessionId);
        return ResponseEntity.ok(Map.of("status", "Session ended"));
    }

    @GetMapping("/{sessionId}/history")
    public ResponseEntity<List<MessageDTO>> getHistory(
            @PathVariable Long sessionId,
            @RequestParam @NotBlank String customerSessionId) {

        return ResponseEntity.ok(chatService.getSessionHistory(sessionId, customerSessionId));
    }
}
