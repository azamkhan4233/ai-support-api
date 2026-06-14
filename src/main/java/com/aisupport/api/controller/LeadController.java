package com.aisupport.api.controller;

import com.aisupport.api.dto.LeadDTO;
import com.aisupport.api.model.Lead;
import com.aisupport.api.model.User;
import com.aisupport.api.repository.UserRepository;
import com.aisupport.api.service.AuthService;
import com.aisupport.api.service.LeadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/leads")
public class LeadController {

    @Autowired
    private LeadService leadService;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<LeadDTO>> getLeads(Authentication authentication) {
        User user = userRepository
                .findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Long businessId = user.getBusiness().getId();

        return ResponseEntity.ok(leadService.getBusinessLeads(businessId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Lead>> getLeadsByStatus(@PathVariable String status) {
        Long businessId = authService.getCurrentUser().getBusiness().getId();
        List<Lead> leads = leadService.getLeadsByStatus(businessId, status);
        return ResponseEntity.ok(leads);
    }

    @PutMapping("/{leadId}/status")
    public ResponseEntity<Lead> updateLeadStatus(
            @PathVariable Long leadId,
            @RequestBody Map<String, String> request) {

        Long businessId = authService.getCurrentUser().getBusiness().getId();
        String newStatus = request.get("status");
        Lead updated = leadService.updateLeadStatus(leadId, businessId, newStatus);
        return ResponseEntity.ok(updated);
    }
}
