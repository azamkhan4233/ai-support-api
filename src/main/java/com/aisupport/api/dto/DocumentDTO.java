package com.aisupport.api.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentDTO {

    private Long id;

    private String fileName;

    private String filePath;

    private LocalDateTime uploadedAt;
}