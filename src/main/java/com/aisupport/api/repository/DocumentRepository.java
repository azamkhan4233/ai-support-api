package com.aisupport.api.repository;

import com.aisupport.api.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    List<Document> findByBusinessId(Long businessId);

}
