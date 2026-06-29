package com.aisupport.api.repository;

import com.aisupport.api.model.DocumentChunk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DocumentChunkRepository extends JpaRepository<DocumentChunk, Long> {

    List<DocumentChunk> findByDocumentIdOrderByChunkIndex(Long documentId);

    // Optimized to fetch Document and Business in a single SQL query
    @Query("SELECT dc FROM DocumentChunk dc " +
            "JOIN FETCH dc.document d " +
            "JOIN FETCH d.business b " +
            "WHERE b.id = :businessId")
    List<DocumentChunk> findByDocumentBusinessId(@Param("businessId") Long businessId);
}