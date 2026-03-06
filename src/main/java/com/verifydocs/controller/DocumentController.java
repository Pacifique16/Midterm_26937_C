package com.verifydocs.controller;

import com.verifydocs.entity.Document;
import com.verifydocs.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {
    
    @Autowired
    private DocumentService documentService;
    
    @PostMapping
    public ResponseEntity<Document> createDocument(@RequestBody Document document) {
        Document saved = documentService.saveDocument(document);
        return ResponseEntity.ok(saved);
    }
    
    // Pagination endpoint
    @GetMapping("/paginated")
    public ResponseEntity<Page<Document>> getDocumentsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(documentService.getAllDocuments(page, size));
    }
    
    // Sorting endpoint
    @GetMapping("/sorted")
    public ResponseEntity<List<Document>> getDocumentsSorted(
            @RequestParam(defaultValue = "issueDate") String sortBy) {
        return ResponseEntity.ok(documentService.getAllDocumentsSorted(sortBy));
    }
    
    // Pagination + Sorting combined
    @GetMapping("/paginated-sorted")
    public ResponseEntity<Page<Document>> getDocumentsPaginatedAndSorted(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "issueDate") String sortBy) {
        return ResponseEntity.ok(documentService.getDocumentsPaginatedAndSorted(page, size, sortBy));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Document> getDocumentById(@PathVariable Long id) {
        return ResponseEntity.ok(documentService.getDocumentById(id));
    }
    
    // Public verification endpoint
    @GetMapping("/verify/{verificationCode}")
    public ResponseEntity<String> verifyDocument(
            @PathVariable String verificationCode,
            @RequestParam(required = false) String ipAddress) {
        String result = documentService.verifyDocument(verificationCode, ipAddress);
        return ResponseEntity.ok(result);
    }
}
