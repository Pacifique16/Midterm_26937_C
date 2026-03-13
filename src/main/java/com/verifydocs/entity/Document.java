package com.verifydocs.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "documents")
public class Document {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String documentType;
    
    @Column(nullable = false)
    private String recipientName;
    
    @Column(nullable = false)
    private String filePath;
    
    @Column(nullable = false, unique = true)
    private String hashValue;
    
    @Column(nullable = false, unique = true)
    private String verificationCode;
    
    @Column(nullable = false)
    private LocalDateTime issueDate;
    
    // Many-to-One: Many Documents belong to one Institution
    @ManyToOne
    @JoinColumn(name = "institution_id", nullable = false)
    private Institution institution;
    
    // Many-to-Many: Documents can be accessed by multiple Users
    @ManyToMany(mappedBy = "accessibleDocuments", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<User> authorizedUsers = new HashSet<>();
    
    // One-to-Many: One Document has many VerificationLogs
    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<VerificationLog> verificationLogs;
    
    public Document() {}
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getDocumentType() {
        return documentType;
    }
    
    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }
    
    public String getRecipientName() {
        return recipientName;
    }
    
    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }
    
    public String getFilePath() {
        return filePath;
    }
    
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    
    public String getHashValue() {
        return hashValue;
    }
    
    public void setHashValue(String hashValue) {
        this.hashValue = hashValue;
    }
    
    public String getVerificationCode() {
        return verificationCode;
    }
    
    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }
    
    public LocalDateTime getIssueDate() {
        return issueDate;
    }
    
    public void setIssueDate(LocalDateTime issueDate) {
        this.issueDate = issueDate;
    }
    
    public Institution getInstitution() {
        return institution;
    }
    
    public void setInstitution(Institution institution) {
        this.institution = institution;
    }
    
    public Set<User> getAuthorizedUsers() {
        return authorizedUsers;
    }
    
    public void setAuthorizedUsers(Set<User> authorizedUsers) {
        this.authorizedUsers = authorizedUsers;
    }
    
    public List<VerificationLog> getVerificationLogs() {
        return verificationLogs;
    }
    
    public void setVerificationLogs(List<VerificationLog> verificationLogs) {
        this.verificationLogs = verificationLogs;
    }
}
