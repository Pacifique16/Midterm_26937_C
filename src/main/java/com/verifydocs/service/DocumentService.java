package com.verifydocs.service;

import com.verifydocs.entity.Document;
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
        document.setIssueDate(LocalDateTime.now());
        document.setVerificationCode(generateVerificationCode());
        return documentRepository.save(document);
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
    
    // Verify document by comparing hashes
    public String verifyDocument(String verificationCode, String ipAddress) {
        Document document = documentRepository.findByVerificationCode(verificationCode);
        
        if (document == null) {
            logVerification(null, "FAILED - Document not found", ipAddress);
            return "FAILED - Document not found";
        }
        
        // Log verification
        logVerification(document, "SUCCESS - Document is authentic", ipAddress);
        return "SUCCESS - Document is authentic";
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
}
