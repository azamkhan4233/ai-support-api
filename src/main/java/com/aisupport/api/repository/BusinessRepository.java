package com.aisupport.api.repository;

import com.aisupport.api.model.Business;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {
    Optional<Business> findByContactEmail(String email);

    Optional<Business> findByPublicApiKey(String apiKey);
}
