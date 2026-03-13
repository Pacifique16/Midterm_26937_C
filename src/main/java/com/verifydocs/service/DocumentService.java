package com.verifydocs.service;

import com.verifydocs.entity.Document;
import com.verifydocs.entity.InstitutionProfile;
import com.verifydocs.entity.VerificationLog;
import com.verifydocs.repository.DocumentRepository;
import com.verifydocs.repository.VerificationLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class DocumentService {
    
    @Autowired
    private DocumentRepository documentRepository;
    
    @Autowired
    private VerificationLogRepository verificationLogRepository;
    
    // Generate SHA-256 hash for document
    public String generateHash(String content) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(content.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error generating hash", e);
        }
    }
    
    // Generate unique verification code
    public String generateVerificationCode() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    public Document saveDocument(Document document) {
        // Generate hash from document content
        String content = document.getDocumentType() + document.getRecipientName() + 
                        document.getFilePath() + LocalDateTime.now().toString();
        document.setHashValue(generateHash(content));
        
        document.setIssueDate(LocalDateTime.now());
        document.setVerificationCode(generateVerificationCode());
        return documentRepository.save(document);
    }
    
    // Get all documents without pagination
    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }
    
    // Pagination - retrieves documents page by page
    public Page<Document> getAllDocuments(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return documentRepository.findAll(pageable);
    }
    
    // Sorting - retrieves documents sorted by a field
    public List<Document> getAllDocumentsSorted(String sortBy) {
        Sort sort = Sort.by(sortBy).descending();
        return documentRepository.findAll(sort);
    }
    
    // Pagination + Sorting combined
    public Page<Document> getDocumentsPaginatedAndSorted(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        return documentRepository.findAll(pageable);
    }
    
    public Page<Document> getDocumentsByInstitution(Long institutionId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return documentRepository.findByInstitutionId(institutionId, pageable);
    }
    
    // Verify document authenticity
    public String verifyDocument(String verificationCode, String ipAddress) {
        Document document = documentRepository.findByVerificationCode(verificationCode);
        
        if (document == null) {
            logVerification(null, "FAILED - Document not found. Invalid verification code.", ipAddress);
            return "FRAUD DETECTED!\n\nThis verification code does not exist in our system.\nThe document is FAKE or the verification code is incorrect.";
        }
        
        // Format verification response
        String response = formatVerificationResponse(document);
        
        // Log verification
        logVerification(document, "SUCCESS - Document is authentic", ipAddress);
        return response;
    }
    
    // Advanced verification with hash comparison to detect tampering
    public String verifyDocumentWithHash(String verificationCode, String documentContent, String ipAddress) {
        Document document = documentRepository.findByVerificationCode(verificationCode);
        
        if (document == null) {
            logVerification(null, "FAILED - Document not found. Invalid verification code.", ipAddress);
            return "FRAUD DETECTED!\n\nThis verification code does not exist in our system.\nThe document is FAKE or the verification code is incorrect.";
        }
        
        // Recompute hash from provided document content
        String computedHash = generateHash(documentContent);
        
        // Compare with stored hash
        if (!computedHash.equals(document.getHashValue())) {
            logVerification(document, "FAILED - Document has been tampered. Hash mismatch.", ipAddress);
            return "FRAUD DETECTED!\n\n" +
                   "This document has been TAMPERED or MODIFIED.\n\n" +
                   "Original Hash: " + document.getHashValue().substring(0, 16) + "...\n" +
                   "Current Hash:  " + computedHash.substring(0, 16) + "...\n\n" +
                   "The document content does not match our records.\n" +
                   "This document is NOT AUTHENTIC.";
        }
        
        // Document is authentic
        String response = formatVerificationResponse(document);
        logVerification(document, "SUCCESS - Document is authentic and untampered", ipAddress);
        return response;
    }
    
    private String formatVerificationResponse(Document document) {
        StringBuilder response = new StringBuilder();
        response.append("✓ VERIFIED - Document is AUTHENTIC\n");
        response.append("\n");
        response.append("Document Type: ").append(document.getDocumentType()).append("\n");
        response.append("Recipient Name: ").append(document.getRecipientName()).append("\n");
        response.append("Issue Date: ").append(document.getIssueDate().toLocalDate()).append("\n");
        response.append("\n");
        response.append("Issued By:\n");
        response.append("Institution: ").append(document.getInstitution().getName()).append("\n");
        response.append("Email: ").append(document.getInstitution().getEmail()).append("\n");
        
        // Build address from location hierarchy and profile
        response.append("Address: ");
        
        // Get street from profile if available
        if (document.getInstitution().getProfile() != null && 
            document.getInstitution().getProfile().getStreet() != null) {
            response.append(document.getInstitution().getProfile().getStreet()).append(", ");
        }
        
        // Get district and province from location hierarchy
        if (document.getInstitution().getLocation() != null) {
            // Navigate: Village -> Cell -> Sector -> District -> Province
            var village = document.getInstitution().getLocation();
            if (village.getParent() != null) {
                var cell = village.getParent();
                if (cell.getParent() != null) {
                    var sector = cell.getParent();
                    if (sector.getParent() != null) {
                        var district = sector.getParent();
                        response.append(district.getName()).append(", ");
                        if (district.getParent() != null) {
                            var province = district.getParent();
                            response.append(province.getName());
                        }
                    }
                }
            }
        }
        response.append("\n");
        response.append("\n");
        response.append("This document has been verified against our secure database.\n");
        
        return response.toString();
    }
    
    private void logVerification(Document document, String result, String ipAddress) {
        VerificationLog log = new VerificationLog();
        log.setDocument(document);
        log.setResult(result);
        log.setIpAddress(ipAddress);
        log.setVerificationTime(LocalDateTime.now());
        verificationLogRepository.save(log);
    }
    
    public Document getDocumentById(Long id) {
        return documentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Document not found"));
    }
    
    public Document updateDocument(Long id, Document updatedDocument) {
        Document existing = documentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Document not found"));
        
        existing.setDocumentType(updatedDocument.getDocumentType());
        existing.setRecipientName(updatedDocument.getRecipientName());
        existing.setFilePath(updatedDocument.getFilePath());
        
        return documentRepository.save(existing);
    }
    
    public void deleteDocument(Long id) {
        if (!documentRepository.existsById(id)) {
            throw new RuntimeException("Document not found");
        }
        documentRepository.deleteById(id);
    }
}
