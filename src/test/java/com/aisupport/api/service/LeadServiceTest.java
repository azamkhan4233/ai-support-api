package com.aisupport.api.service;

import com.aisupport.api.exception.ResourceNotFoundException;
import com.aisupport.api.model.Business;
import com.aisupport.api.model.Lead;
import com.aisupport.api.repository.LeadRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LeadServiceTest {

    @Mock
    private LeadRepository leadRepository;

    @Mock
    private AIService aiService;

    @InjectMocks
    private LeadService leadService;

    @Test
    void updateLeadStatus_rejectsCrossTenantAccess() {
        Business ownerBusiness = Business.builder().id(1L).name("Tenant A").build();
        Business otherBusiness = Business.builder().id(2L).name("Tenant B").build();

        Lead lead = Lead.builder()
                .id(10L)
                .business(otherBusiness)
                .status("NEW")
                .build();

        when(leadRepository.findById(10L)).thenReturn(Optional.of(lead));

        assertThrows(ResourceNotFoundException.class,
                () -> leadService.updateLeadStatus(10L, ownerBusiness.getId(), "CONTACTED"));
    }

    @Test
    void updateLeadStatus_allowsSameTenant() {
        Business business = Business.builder().id(1L).name("Tenant A").build();

        Lead lead = Lead.builder()
                .id(10L)
                .business(business)
                .status("NEW")
                .build();

        when(leadRepository.findById(10L)).thenReturn(Optional.of(lead));
        when(leadRepository.save(lead)).thenReturn(lead);

        leadService.updateLeadStatus(10L, business.getId(), "CONTACTED");
    }
}
