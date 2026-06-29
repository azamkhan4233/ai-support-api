package com.aisupport.api.service;

import com.aisupport.api.dto.AnalyticsDTO;
import com.aisupport.api.repository.ChatSessionRepository;
import com.aisupport.api.repository.LeadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsService {

    @Autowired
    private ChatSessionRepository chatSessionRepository;

    @Autowired
    private LeadRepository leadRepository;

    public AnalyticsDTO getDashboardAnalytics(Long businessId) {

        long totalChats =
                chatSessionRepository.countByBusinessId(businessId);

        long activeChats =
                chatSessionRepository.countByBusinessIdAndStatus(
                        businessId,
                        "ACTIVE"
                );

        long totalLeads =
                leadRepository.countByBusinessId(businessId);

        long newLeads =
                leadRepository.countByBusinessIdAndStatus(
                        businessId,
                        "NEW"
                );

        long contactedLeads =
                leadRepository.countByBusinessIdAndStatus(
                        businessId,
                        "CONTACTED"
                );

        long qualifiedLeads =
                leadRepository.countByBusinessIdAndStatus(
                        businessId,
                        "QUALIFIED"
                );

        long closedLeads =
                leadRepository.countByBusinessIdAndStatus(
                        businessId,
                        "CLOSED"
                );

        double conversionRate = 0;

        if (totalChats > 0) {
            conversionRate =
                    ((double) qualifiedLeads / totalChats) * 100;
        }

        return AnalyticsDTO.builder()
                .totalChats(totalChats)
                .activeChats(activeChats)
                .totalLeads(totalLeads)
                .newLeads(newLeads)
                .contactedLeads(contactedLeads)
                .qualifiedLeads(qualifiedLeads)
                .closedLeads(closedLeads)
                .conversionRate(
                        Math.round(conversionRate * 100.0) / 100.0
                )
                .build();
    }
}