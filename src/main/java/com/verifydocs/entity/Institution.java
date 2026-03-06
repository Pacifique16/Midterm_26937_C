package com.verifydocs.entity;

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
    
    // Many-to-One: Many Institutions belong to one Province
    @ManyToOne
    @JoinColumn(name = "province_id", nullable = false)
    private Province province;
    
    // One-to-Many: One Institution has many Users
    @OneToMany(mappedBy = "institution", cascade = CascadeType.ALL)
    private List<User> users;
    
    // One-to-Many: One Institution issues many Documents
    @OneToMany(mappedBy = "institution", cascade = CascadeType.ALL)
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
    
    public Province getProvince() {
        return province;
    }
    
    public void setProvince(Province province) {
        this.province = province;
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
