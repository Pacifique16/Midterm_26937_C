package com.verifydocs.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    @Column(nullable = false)
    private String role;
    
    private String fullName;
    
    // Many-to-One: Many Users belong to one Institution
    @ManyToOne
    @JoinColumn(name = "institution_id", nullable = false)
    private Institution institution;
    
    // Many-to-Many: Users can access multiple Documents
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "user_document_access",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "document_id")
    )
    @JsonIgnore
    private Set<Document> accessibleDocuments = new HashSet<>();
    
    public User() {}
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public Institution getInstitution() {
        return institution;
    }
    
    public void setInstitution(Institution institution) {
        this.institution = institution;
    }
    
    public Set<Document> getAccessibleDocuments() {
        return accessibleDocuments;
    }
    
    public void setAccessibleDocuments(Set<Document> accessibleDocuments) {
        this.accessibleDocuments = accessibleDocuments;
    }
}
