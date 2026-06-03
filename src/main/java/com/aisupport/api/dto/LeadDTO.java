package com.aisupport.api.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeadDTO {

    private Long id;

    private String name;
    private String email;
    private String phone;

    private String interest;
    private String status;

    private LocalDateTime createdAt;
}