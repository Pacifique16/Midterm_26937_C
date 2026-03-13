package com.verifydocs.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "institutions")
public class Institution {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String status;
    
    // Many-to-One: Many Institutions belong to one Location (Village)
    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;
    
    // One-to-Many: One Institution has many Users
    @OneToMany(mappedBy = "institution", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<User> users;
    
    // One-to-Many: One Institution issues many Documents
    @OneToMany(mappedBy = "institution", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Document> documents;
    
    // One-to-One: One Institution has one Profile
    @OneToOne(mappedBy = "institution", cascade = CascadeType.ALL)
    private InstitutionProfile profile;
    
    public Institution() {}
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Location getLocation() {
        return location;
    }
    
    public void setLocation(Location location) {
        this.location = location;
    }
    
    public List<User> getUsers() {
        return users;
    }
    
    public void setUsers(List<User> users) {
        this.users = users;
    }
    
    public List<Document> getDocuments() {
        return documents;
    }
    
    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }
    
    public InstitutionProfile getProfile() {
        return profile;
    }
    
    public void setProfile(InstitutionProfile profile) {
        this.profile = profile;
    }
}
