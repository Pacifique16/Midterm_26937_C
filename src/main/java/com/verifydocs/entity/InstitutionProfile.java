package com.verifydocs.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "institution_profiles")
public class InstitutionProfile {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Location fields (stored but only street, district, province displayed)
    private String street;          // e.g., "KG 544 St"
    private String district;        // e.g., "Gasabo"
    private String sector;          // Stored but not displayed
    private String cell;            // Stored but not displayed
    private String village;         // Stored but not displayed
    private String provinceName;    // e.g., "Kigali" (for display)
    
    private String phone;
    private String website;
    private String description;
    
    // One-to-One: One Profile belongs to one Institution
    @OneToOne
    @JoinColumn(name = "institution_id", unique = true, nullable = false)
    @JsonIgnore
    private Institution institution;
    
    public InstitutionProfile() {}
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getStreet() {
        return street;
    }
    
    public void setStreet(String street) {
        this.street = street;
    }
    
    public String getDistrict() {
        return district;
    }
    
    public void setDistrict(String district) {
        this.district = district;
    }
    
    public String getSector() {
        return sector;
    }
    
    public void setSector(String sector) {
        this.sector = sector;
    }
    
    public String getCell() {
        return cell;
    }
    
    public void setCell(String cell) {
        this.cell = cell;
    }
    
    public String getVillage() {
        return village;
    }
    
    public void setVillage(String village) {
        this.village = village;
    }
    
    public String getProvinceName() {
        return provinceName;
    }
    
    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
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
