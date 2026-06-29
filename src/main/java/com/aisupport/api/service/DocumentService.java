package com.aisupport.api.service;

import com.aisupport.api.model.Business;
import com.aisupport.api.model.Document;
import com.aisupport.api.repository.DocumentChunkRepository;
import com.aisupport.api.repository.DocumentRepository;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.*;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import com.aisupport.api.model.DocumentChunk;
import com.aisupport.api.repository.DocumentChunkRepository;

@Service
public class DocumentService {

    private static final String UPLOAD_DIR = "uploads";

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private DocumentChunkRepository documentChunkRepository;

    public Document uploadDocument(Business business,
                                   MultipartFile file) throws IOException {

        // Create uploads folder if it doesn't exist
        Path uploadPath = Paths.get(UPLOAD_DIR);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Generate unique filename
        String fileName =
                System.currentTimeMillis() + "_"
                        + file.getOriginalFilename();

        // Full path
        Path filePath = uploadPath.resolve(fileName);

        // Save PDF
        Files.copy(file.getInputStream(), filePath);

        // Save metadata
        Document document = Document.builder()
                .business(business)
                .fileName(file.getOriginalFilename())
                .filePath(filePath.toString())
                .uploadedAt(LocalDateTime.now())
                .build();

        // Save first
        document = documentRepository.save(document);

        String extractedText = extractText(filePath);

        List<String> chunks = splitIntoChunks(extractedText);

        saveChunks(document, chunks);



        return document;
    }

    private String extractText(Path pdfPath) throws IOException {

        try (PDDocument document = Loader.loadPDF(pdfPath.toFile())) {

            PDFTextStripper stripper = new PDFTextStripper();

            return stripper.getText(document);
        }
    }


    private List<String> splitIntoChunks(String text) {

        List<String> chunks = new ArrayList<>();

        int chunkSize = 500;

        int start = 0;

        while (start < text.length()) {

            int end = Math.min(start + chunkSize, text.length());

            // Don't cut words/sentences if we haven't reached the end
            if (end < text.length()) {

                int lastNewLine = text.lastIndexOf('\n', end);
                int lastPeriod = text.lastIndexOf('.', end);

                int splitPoint = Math.max(lastNewLine, lastPeriod);

                // Only use it if it's reasonably close
                if (splitPoint > start + 200) {
                    end = splitPoint + 1;
                }
            }

            chunks.add(text.substring(start, end).trim());

            start = end;
        }

        return chunks;
    }


    private void saveChunks(Document document, List<String> chunks) {

        for (int i = 0; i < chunks.size(); i++) {

            DocumentChunk chunk = DocumentChunk.builder()
                    .document(document)
                    .chunkIndex(i)
                    .content(chunks.get(i))
                    .build();

            documentChunkRepository.save(chunk);
        }
    }

    public String retrieveRelevantKnowledge(Long businessId, String question) {

        List<DocumentChunk> chunks =
                documentChunkRepository.findByDocumentBusinessId(businessId);

        if (chunks.isEmpty()) {
            return "";
        }

        Set<String> stopWords = Set.of(
                "the", "a", "an", "is", "are",
                "to", "in", "of", "and", "it",
                "what", "how", "do", "does",
                "can", "i", "you", "your"
        );

        List<String> keywords = Arrays.stream(
                        question.toLowerCase().split("\\W+"))
                .filter(word ->
                        word.length() > 1 &&
                                !stopWords.contains(word))
                .toList();

        Map<DocumentChunk, Integer> scores = new HashMap<>();

        for (DocumentChunk chunk : chunks) {

            Set<String> chunkWords =
                    new HashSet<>(
                            Arrays.asList(
                                    chunk.getContent()
                                            .toLowerCase()
                                            .split("\\W+")
                            )
                    );

            int score = 0;

            for (String keyword : keywords) {

                if (chunkWords.contains(keyword)) {
                    score++;
                }
            }

            if (score > 0) {
                scores.put(chunk, score);
            }
        }

        if (scores.isEmpty()) {
            return "";
        }

        List<DocumentChunk> topChunks =
                scores.entrySet()
                        .stream()
                        .sorted((a, b) ->
                                Integer.compare(
                                        b.getValue(),
                                        a.getValue()
                                ))
                        .limit(3)
                        .map(Map.Entry::getKey)
                        .toList();

        StringBuilder knowledge = new StringBuilder();

        for (DocumentChunk chunk : topChunks) {

            knowledge.append(chunk.getContent())
                    .append("\n\n-------------------------\n\n");
        }

        return knowledge.toString();
    }
}