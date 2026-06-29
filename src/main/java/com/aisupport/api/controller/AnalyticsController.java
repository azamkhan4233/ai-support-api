package com.aisupport.api.controller;

import com.aisupport.api.dto.AnalyticsDTO;
import com.aisupport.api.service.AnalyticsService;
import com.aisupport.api.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analytics")
@CrossOrigin(origins = "*")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    @Autowired
    private AuthService authService;

    @GetMapping("/dashboard")
    public ResponseEntity<AnalyticsDTO> getDashboardAnalytics() {

        Long businessId = authService
                .getCurrentUser()
                .getBusiness()
                .getId();

        return ResponseEntity.ok(
                analyticsService.getDashboardAnalytics(
                        businessId
                )
        );
    }
}