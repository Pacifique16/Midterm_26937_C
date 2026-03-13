package com.verifydocs.controller;

import com.verifydocs.entity.Document;
import com.verifydocs.service.DocumentService;
import com.verifydocs.service.QRCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {
    
    @Autowired
    private DocumentService documentService;
    
    @Autowired
    private QRCodeService qrCodeService;
    
    @PostMapping
    public ResponseEntity<Document> createDocument(@RequestBody Document document) {
        Document saved = documentService.saveDocument(document);
        return ResponseEntity.ok(saved);
    }
    
    @GetMapping
    public ResponseEntity<List<Document>> getAllDocuments() {
        return ResponseEntity.ok(documentService.getAllDocuments());
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
    
    // Public verification endpoint using path variable
    @GetMapping("/verify/{verificationCode}")
    public ResponseEntity<String> verifyDocument(
            @PathVariable String verificationCode,
            @RequestParam(required = false) String ipAddress) {
        String result = documentService.verifyDocument(verificationCode, ipAddress);
        return ResponseEntity.ok(result);
    }
    
    // Alternative verification endpoint using query parameter
    @GetMapping("/verify")
    public ResponseEntity<String> verifyDocumentByParam(
            @RequestParam String verification_code,
            @RequestParam(required = false) String ipAddress) {
        String result = documentService.verifyDocument(verification_code, ipAddress);
        return ResponseEntity.ok(result);
    }
    
    // Advanced verification with hash comparison to detect tampering
    @PostMapping("/verify-with-content")
    public ResponseEntity<String> verifyDocumentWithContent(
            @RequestParam String verificationCode,
            @RequestBody String documentContent,
            @RequestParam(required = false) String ipAddress) {
        String result = documentService.verifyDocumentWithHash(verificationCode, documentContent, ipAddress);
        return ResponseEntity.ok(result);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Document> updateDocument(@PathVariable Long id, @RequestBody Document document) {
        return ResponseEntity.ok(documentService.updateDocument(id, document));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDocument(@PathVariable Long id) {
        documentService.deleteDocument(id);
        return ResponseEntity.ok("Document deleted successfully");
    }
    
    // Generate QR Code for document verification
    @GetMapping("/{id}/qrcode")
    public ResponseEntity<byte[]> generateQRCode(
            @PathVariable Long id,
            @RequestParam(defaultValue = "300") int width,
            @RequestParam(defaultValue = "300") int height) {
        Document document = documentService.getDocumentById(id);
        byte[] qrCode = qrCodeService.generateQRCode(document.getVerificationCode(), width, height);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        
        return new ResponseEntity<>(qrCode, headers, HttpStatus.OK);
    }
    
    // Generate QR Code with document details
    @GetMapping("/{id}/qrcode-detailed")
    public ResponseEntity<byte[]> generateDetailedQRCode(
            @PathVariable Long id,
            @RequestParam(defaultValue = "400") int width,
            @RequestParam(defaultValue = "400") int height) {
        Document document = documentService.getDocumentById(id);
        byte[] qrCode = qrCodeService.generateQRCodeWithData(
            document.getVerificationCode(),
            document.getDocumentType(),
            document.getRecipientName(),
            width, height
        );
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        
        return new ResponseEntity<>(qrCode, headers, HttpStatus.OK);
    }
    
    // Generate FAKE QR Code for fraud detection testing
    @GetMapping("/qrcode-fake")
    public ResponseEntity<byte[]> generateFakeQRCode(
            @RequestParam(defaultValue = "300") int width,
            @RequestParam(defaultValue = "300") int height) {
        byte[] qrCode = qrCodeService.generateQRCode("FAKE1234", width, height);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        
        return new ResponseEntity<>(qrCode, headers, HttpStatus.OK);
    }
}
