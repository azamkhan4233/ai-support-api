package com.aisupport.api.service;

import com.aisupport.api.model.Business;
import com.aisupport.api.repository.BusinessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class BusinessService {

    @Autowired
    private BusinessRepository businessRepository;

    public Business createBusiness(Business business) {

        business.setPublicApiKey(generateApiKey());
        return businessRepository.save(business);
    }

    public Business getBusinessByApiKey(String apiKey) {
        return businessRepository.findByPublicApiKey(apiKey)
                .orElseThrow(() -> new RuntimeException("Invalid API Key"));
    }

    private String generateApiKey() {
        byte[] randomBytes = new byte[32];
        new SecureRandom().nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }
}
