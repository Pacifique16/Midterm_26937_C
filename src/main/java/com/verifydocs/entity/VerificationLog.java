package com.verifydocs.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "verification_logs")
public class VerificationLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private LocalDateTime verificationTime;
    
    private String ipAddress;
    
    @Column(nullable = false, length = 500)
    private String result;
    
    // Many-to-One: Many VerificationLogs belong to one Document
    @ManyToOne
    @JoinColumn(name = "document_id", nullable = true)
    private Document document;
    
    public VerificationLog() {}
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public LocalDateTime getVerificationTime() {
        return verificationTime;
    }
    
    public void setVerificationTime(LocalDateTime verificationTime) {
        this.verificationTime = verificationTime;
    }
    
    public String getIpAddress() {
        return ipAddress;
    }
    
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    
    public String getResult() {
        return result;
    }
    
    public void setResult(String result) {
        this.result = result;
    }
    
    public Document getDocument() {
        return document;
    }
    
    public void setDocument(Document document) {
        this.document = document;
    }
}
