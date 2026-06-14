package com.aisupport.api.service;

import com.aisupport.api.dto.JwtResponseDTO;
import com.aisupport.api.dto.LoginRequestDTO;
import com.aisupport.api.dto.RegisterRequestDTO;
import com.aisupport.api.model.Business;
import com.aisupport.api.model.User;
import com.aisupport.api.repository.BusinessRepository;
import com.aisupport.api.repository.UserRepository;
import com.aisupport.api.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;
 @Autowired
    private  BusinessRepository businessRepository;

    public JwtResponseDTO login(LoginRequestDTO request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return JwtResponseDTO.builder()
                .token(jwt)
                .type("Bearer")
                .userId(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .businessId(user.getBusiness() != null ? user.getBusiness().getId() : null)
                .build();
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void register(RegisterRequestDTO request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException(
                    "Email already registered");
        }

        Business business = new Business();
        business.setName(request.getBusinessName());
        business.setContactEmail(request.getEmail()); // add this
        business.setActive(true);                      // add this
        business.setPublicApiKey(UUID.randomUUID().toString()); // add this
        business.setCreatedAt(LocalDateTime.now());    // add this

        business = businessRepository.save(business);

        User user = new User();
        user.setEmail(request.getEmail());
        user.setName(request.getBusinessName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("ADMIN");
        user.setBusiness(business);
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);
    }

    public String forgotPassword(String email) {
        System.out.println("EMAIL RECEIVED: [" + email + "]");
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("No account found with this email"));

        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        user.setResetTokenExpiry(LocalDateTime.now().plusHours(1));
        userRepository.save(user);

        // TODO: wire EmailService here later to send token via email
        return token;
    }

    public void resetPassword(String token, String newPassword) {
        User user = userRepository.findByResetToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid or expired reset token"));

        if (user.getResetTokenExpiry() == null ||
                user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Reset token has expired");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        userRepository.save(user);
    }
}

