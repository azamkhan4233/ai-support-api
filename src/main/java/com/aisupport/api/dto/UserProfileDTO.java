package com.aisupport.api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProfileDTO {

    private Long id;
    private String email;
    private String name;
    private String role;

    private Long businessId;
    private String businessName;
}