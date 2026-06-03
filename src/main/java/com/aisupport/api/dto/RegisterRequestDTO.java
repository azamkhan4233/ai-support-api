package com.aisupport.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

    @Getter
    @Setter
    public class RegisterRequestDTO {

        @NotBlank
        private String businessName;

        @Email
        @NotBlank
        private String email;

        @NotBlank
        @Size(min = 8, max = 100)
        private String password;
    }

