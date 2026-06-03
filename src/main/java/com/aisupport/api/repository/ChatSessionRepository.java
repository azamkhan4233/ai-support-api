package com.aisupport.api.repository;

import com.aisupport.api.model.ChatSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatSessionRepository extends JpaRepository<ChatSession, Long> {
    List<ChatSession> findByBusinessIdOrderByStartedAtDesc(Long businessId);
    List<ChatSession> findByCustomerIdOrderByStartedAtDesc(Long customerId);
    List<ChatSession> findByStatus(String status);
}
