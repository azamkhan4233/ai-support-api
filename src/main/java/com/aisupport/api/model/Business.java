package com.aisupport.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "businesses")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Business {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String industry;

    @Column(nullable = false)
    private String contactEmail;

    @Column(columnDefinition = "TEXT")
    private String agentSystemPrompt;

    @Column(columnDefinition = "TEXT")
    private String agentPersonality;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL)
    private List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL)
    private List<Customer> customers = new ArrayList<>();

    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL)
    private List<Lead> leads = new ArrayList<>();

    private boolean active = true;

    @Column(name = "public_api_key")
    private String publicApiKey;
}
