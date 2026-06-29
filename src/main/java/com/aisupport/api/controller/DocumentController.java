package com.aisupport.api.controller;

import com.aisupport.api.dto.DocumentDTO;
import com.aisupport.api.model.Business;
import com.aisupport.api.model.Document;
import com.aisupport.api.service.AuthService;
import com.aisupport.api.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/documents")
@CrossOrigin(origins = "*")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private AuthService authService;

    @PostMapping("/upload")
    public ResponseEntity<DocumentDTO> uploadDocument(
            @RequestParam("file") MultipartFile file) {

        try {

            Business business = authService
                    .getCurrentUser()
                    .getBusiness();

            Document document =
                    documentService.uploadDocument(
                            business,
                            file
                    );

            DocumentDTO dto = DocumentDTO.builder()
                    .id(document.getId())
                    .fileName(document.getFileName())
                    .filePath(document.getFilePath())
                    .uploadedAt(document.getUploadedAt())
                    .build();

            return ResponseEntity.ok(dto);

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .build();
        }
    }
}

