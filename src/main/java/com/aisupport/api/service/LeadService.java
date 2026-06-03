package com.aisupport.api.service;

import com.aisupport.api.model.ChatSession;
import com.aisupport.api.model.Lead;
import com.aisupport.api.model.Message;
import com.aisupport.api.dto.LeadDTO;
import com.aisupport.api.repository.LeadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class LeadService {

    @Autowired
    private LeadRepository leadRepository;

    @Autowired
    private AIService aiService;

    public void tryExtractLead(ChatSession session, List<Message> conversationHistory) {
        try {
            // Only extract if conversation has at least 3 messages
            if (conversationHistory.size() < 3) {
                return;
            }

            Map<String, String> leadInfo = aiService.extractLeadInfo(conversationHistory);

            // Check if we have minimum required info (at least name or email)
            String name = leadInfo.get("name");
            String email = leadInfo.get("email");
            String phone = leadInfo.get("phone");
            String interest = leadInfo.get("interest");

            if ((name != null && !name.equals("null")) || (email != null && !email.equals("null"))) {
                // Check if lead already exists for this business with this email
                if (email != null && !email.equals("null")) {
                    List<Lead> existingLeads = leadRepository.findByBusinessIdOrderByCreatedAtDesc(
                            session.getBusiness().getId());
                    
                    boolean exists = existingLeads.stream()
                            .anyMatch(lead -> email.equals(lead.getEmail()));
                    
                    if (exists) {
                        return; // Don't create duplicate
                    }
                }

                Lead lead = Lead.builder()
                        .business(session.getBusiness())
                        .name(name != null && !name.equals("null") ? name : null)
                        .email(email != null && !email.equals("null") ? email : null)
                        .phone(phone != null && !phone.equals("null") ? phone : null)
                        .interest(interest != null && !interest.equals("null") ? interest : null)
                        .status("NEW")
                        .build();

                leadRepository.save(lead);
            }
        } catch (Exception e) {
            // Log error but don't fail the main chat flow
            e.printStackTrace();
        }
    }


    public List<LeadDTO> getBusinessLeads(Long businessId) {

        return leadRepository
                .findByBusinessIdOrderByCreatedAtDesc(businessId)
                .stream()
                .map(lead -> LeadDTO.builder()
                        .id(lead.getId())
                        .name(lead.getName())
                        .email(lead.getEmail())
                        .phone(lead.getPhone())
                        .interest(lead.getInterest())
                        .status(lead.getStatus())
                        .createdAt(lead.getCreatedAt())
                        .build())
                .toList();
    }

    public List<Lead> getLeadsByStatus(Long businessId, String status) {
        return leadRepository.findByBusinessIdAndStatus(businessId, status);
    }

    public Lead updateLeadStatus(Long leadId, String newStatus) {
        Lead lead = leadRepository.findById(leadId)
                .orElseThrow(() -> new RuntimeException("Lead not found"));
        lead.setStatus(newStatus);
        return leadRepository.save(lead);
    }
}
