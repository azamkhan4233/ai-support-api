package com.aisupport.api.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatSessionDTO {

    private Long sessionId;

    private String customerSessionId;

    private String status;

    private LocalDateTime startedAt;
    private LocalDateTime endedAt;

    private Long customerId;
}