package com.aisupport.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRequestDTO {
    
    @NotBlank(message = "Customer session ID is required")
    private String customerSessionId;

    @NotBlank(message = "Content is required")
    private String content;
    
    private String sender; // defaults to "user"
    
    private String customerName;
    private String customerEmail;
}
