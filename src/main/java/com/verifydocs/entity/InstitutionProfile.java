package com.verifydocs.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "institution_profiles")
public class InstitutionProfile {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String address;
    private String phone;
    private String website;
    private String description;
    
    // One-to-One: One Profile belongs to one Institution
    @OneToOne
    @JoinColumn(name = "institution_id", unique = true, nullable = false)
    private Institution institution;
    
    public InstitutionProfile() {}
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getWebsite() {
        return website;
    }
    
    public void setWebsite(String website) {
        this.website = website;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Institution getInstitution() {
        return institution;
    }
    
    public void setInstitution(Institution institution) {
        this.institution = institution;
    }
}
