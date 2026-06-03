package com.aisupport.api.repository;

import com.aisupport.api.model.Lead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeadRepository extends JpaRepository<Lead, Long> {
    List<Lead> findByBusinessIdOrderByCreatedAtDesc(Long businessId);
    List<Lead> findByStatus(String status);
    List<Lead> findByBusinessIdAndStatus(Long businessId, String status);
}
