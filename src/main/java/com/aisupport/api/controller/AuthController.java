package com.aisupport.api.controller;

import com.aisupport.api.dto.*;
import com.aisupport.api.model.User;
import com.aisupport.api.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        JwtResponseDTO response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileDTO> getCurrentUser() {
        User user = authService.getCurrentUser();

        UserProfileDTO response = UserProfileDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole())
                .businessId(user.getBusiness().getId())
                .businessName(user.getBusiness().getName())
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(
            @Valid @RequestBody RegisterRequestDTO request) {

        authService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new RegisterResponseDTO("Business registered successfully"));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequestDTO request) {

        String token = authService.forgotPassword(request.getEmail());

        return ResponseEntity.ok(Map.of(
                "message", "Password reset token generated",
                "resetToken", token   // REMOVE this line in production
        ));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(
            @Valid @RequestBody ResetPasswordRequestDTO request) {

        authService.resetPassword(request.getToken(), request.getNewPassword());

        return ResponseEntity.ok(Map.of("message", "Password reset successfully"));
    }
}
