package com.aisupport.api.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusinessDTO {

    private Long id;
    private String name;
    private String description;
    private String industry;
    private String contactEmail;

    private String agentSystemPrompt;
    private String agentPersonality;

    private boolean active;
    private String publicApiKey;

    private LocalDateTime createdAt;
}