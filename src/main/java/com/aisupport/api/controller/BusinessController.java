package com.aisupport.api.controller;

import com.aisupport.api.dto.BusinessDTO;
import com.aisupport.api.dto.ChatSessionDTO;
import com.aisupport.api.model.Business;
import com.aisupport.api.repository.BusinessRepository;
import com.aisupport.api.service.AuthService;
import com.aisupport.api.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/business")
public class BusinessController {

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private ChatService chatService;

    @GetMapping("/profile")
    public ResponseEntity<BusinessDTO> getProfile() {
        Long businessId = authService.getCurrentUser().getBusiness().getId();

        Business business = businessRepository
                .findById(businessId)
                .orElseThrow(() -> new RuntimeException("Business not found"));

        BusinessDTO dto = BusinessDTO.builder()
                .id(business.getId())
                .name(business.getName())
                .description(business.getDescription())
                .industry(business.getIndustry())
                .contactEmail(business.getContactEmail())
                .agentSystemPrompt(business.getAgentSystemPrompt())
                .agentPersonality(business.getAgentPersonality())
                .active(business.isActive())
                .publicApiKey(business.getPublicApiKey())
                .createdAt(business.getCreatedAt())
                .build();

        return ResponseEntity.ok(dto);
    }

    @PutMapping("/profile")
    public ResponseEntity<Business> updateProfile(@RequestBody Map<String, String> updates) {
        Long businessId = authService.getCurrentUser().getBusiness().getId();
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new RuntimeException("Business not found"));

        if (updates.containsKey("name")) {
            business.setName(updates.get("name"));
        }
        if (updates.containsKey("description")) {
            business.setDescription(updates.get("description"));
        }
        if (updates.containsKey("industry")) {
            business.setIndustry(updates.get("industry"));
        }
        if (updates.containsKey("agentSystemPrompt")) {
            business.setAgentSystemPrompt(updates.get("agentSystemPrompt"));
        }

        Business updated = businessRepository.save(business);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/sessions")
    public ResponseEntity<List<ChatSessionDTO>> getSessions() {
        Long businessId = authService.getCurrentUser().getBusiness().getId();
        List<ChatSessionDTO> sessions = chatService.getBusinessSessions(businessId);
        return ResponseEntity.ok(sessions);
    }
}
