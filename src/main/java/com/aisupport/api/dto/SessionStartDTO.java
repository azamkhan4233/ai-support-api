package com.aisupport.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionStartDTO {
    
    @NotNull(message = "Business ID is required")
    private Long businessId;
    
    private String customerSessionId; // Optional: for returning customers
    private String ipAddress;
}
