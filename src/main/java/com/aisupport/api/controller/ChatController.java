package com.aisupport.api.controller;

import com.aisupport.api.dto.*;
import com.aisupport.api.model.ChatSession;
import com.aisupport.api.model.Message;
import com.aisupport.api.service.AuthService;
import com.aisupport.api.service.ChatService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
public class ChatController {

    @Autowired
    private ChatService chatService;
    @Autowired
   private AuthService authService;
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
        
        ChatResponseDTO response = chatService.sendMessage(sessionId, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{sessionId}/end")
    public ResponseEntity<Map<String, String>> endSession(@PathVariable Long sessionId) {
        chatService.endSession(sessionId);
        return ResponseEntity.ok(Map.of("status", "Session ended"));
    }

    @GetMapping("/{sessionId}/history")

    public ResponseEntity<List<MessageDTO>> getHistory(
            @PathVariable Long sessionId) {

        return ResponseEntity.ok(
                chatService.getSessionHistory(sessionId));
    }
    @GetMapping("/sessions")
    public ResponseEntity<List<ChatSessionDTO>> getSessions() {

        Long businessId =
                authService.getCurrentUser()
                        .getBusiness()
                        .getId();

        return ResponseEntity.ok(
                chatService.getBusinessSessions(businessId)
        );
    }
}
