package com.verifydocs.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "provinces")
public class Province {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String code;
    
    @Column(nullable = false)
    private String name;
    
    // One-to-Many: One Province has many Institutions
    @OneToMany(mappedBy = "province", cascade = CascadeType.ALL)
    private List<Institution> institutions;
    
    public Province() {}
    
    public Province(String code, String name) {
        this.code = code;
        this.name = name;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public List<Institution> getInstitutions() {
        return institutions;
    }
    
    public void setInstitutions(List<Institution> institutions) {
        this.institutions = institutions;
    }
}
