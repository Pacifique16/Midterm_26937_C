package com.verifydocs.service;

import com.verifydocs.entity.Institution;
import com.verifydocs.entity.InstitutionProfile;
import com.verifydocs.entity.Province;
import com.verifydocs.repository.InstitutionProfileRepository;
import com.verifydocs.repository.InstitutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstitutionService {
    
    @Autowired
    private InstitutionRepository institutionRepository;
    
    @Autowired
    private InstitutionProfileRepository profileRepository;
    
    @Autowired
    private ProvinceService provinceService;
    
    public Institution saveInstitution(Institution institution, String provinceCode) {
        if (institutionRepository.existsByEmail(institution.getEmail())) {
            throw new RuntimeException("Institution with email already exists");
        }
        
        Province province = provinceService.getProvinceByCode(provinceCode);
        if (province == null) {
            throw new RuntimeException("Province not found");
        }
        
        institution.setProvince(province);
        institution.setStatus("ACTIVE");
        return institutionRepository.save(institution);
    }
    
    // One-to-One relationship: Create profile for institution
    public InstitutionProfile createProfile(Long institutionId, InstitutionProfile profile) {
        Institution institution = institutionRepository.findById(institutionId)
            .orElseThrow(() -> new RuntimeException("Institution not found"));
        
        profile.setInstitution(institution);
        return profileRepository.save(profile);
    }
    
    public List<Institution> getAllInstitutions() {
        return institutionRepository.findAll();
    }
    
    public Institution getInstitutionById(Long id) {
        return institutionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Institution not found"));
    }
}
