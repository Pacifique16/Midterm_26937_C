package com.verifydocs.service;

import com.verifydocs.entity.Institution;
import com.verifydocs.entity.InstitutionProfile;
import com.verifydocs.entity.Location;
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
    private LocationService locationService;
    
    public Institution saveInstitution(Institution institution, String villageCode) {
        if (institutionRepository.existsByEmail(institution.getEmail())) {
            throw new RuntimeException("Institution with email already exists");
        }
        
        Location village = locationService.getLocationByCode(villageCode);
        if (!village.getLevel().equals("VILLAGE")) {
            throw new RuntimeException("Location must be a village");
        }
        
        institution.setLocation(village);
        institution.setStatus("ACTIVE");
        return institutionRepository.save(institution);
    }
    
    // One-to-One relationship: Create profile for institution
    public InstitutionProfile createProfile(Long institutionId, InstitutionProfile profile) {
        Institution institution = institutionRepository.findById(institutionId)
            .orElseThrow(() -> new RuntimeException("Institution not found"));
        
        if (profileRepository.existsByInstitutionId(institutionId)) {
            throw new RuntimeException("Institution already has a profile. Use update endpoint instead.");
        }
        
        profile.setInstitution(institution);
        return profileRepository.save(profile);
    }
    
    // Update existing profile
    public InstitutionProfile updateProfile(Long institutionId, InstitutionProfile updatedProfile) {
        Institution institution = institutionRepository.findById(institutionId)
            .orElseThrow(() -> new RuntimeException("Institution not found"));
        
        InstitutionProfile existingProfile = profileRepository.findByInstitutionId(institutionId);
        if (existingProfile == null) {
            throw new RuntimeException("Institution does not have a profile. Use create endpoint instead.");
        }
        
        existingProfile.setStreet(updatedProfile.getStreet());
        existingProfile.setPhone(updatedProfile.getPhone());
        existingProfile.setWebsite(updatedProfile.getWebsite());
        existingProfile.setDescription(updatedProfile.getDescription());
        
        return profileRepository.save(existingProfile);
    }
    
    public void deleteProfile(Long institutionId) {
        InstitutionProfile profile = profileRepository.findByInstitutionId(institutionId);
        if (profile == null) {
            throw new RuntimeException("Institution does not have a profile");
        }
        profileRepository.delete(profile);
    }
    
    public List<Institution> getAllInstitutions() {
        return institutionRepository.findAll();
    }
    
    public Institution getInstitutionById(Long id) {
        return institutionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Institution not found"));
    }
    
    // Retrieve all institutions from a given village
    public List<Institution> getInstitutionsByVillage(String village) {
        return institutionRepository.findAllByVillageName(village);
    }
    
    // Retrieve all institutions from a given cell
    public List<Institution> getInstitutionsByCell(String cell) {
        return institutionRepository.findAllByCellName(cell);
    }
    
    // Retrieve all institutions from a given sector
    public List<Institution> getInstitutionsBySector(String sector) {
        return institutionRepository.findAllBySectorName(sector);
    }
    
    // Retrieve all institutions from a given district
    public List<Institution> getInstitutionsByDistrict(String district) {
        return institutionRepository.findAllByDistrictName(district);
    }
    
    // Retrieve all institutions from a given province name
    public List<Institution> getInstitutionsByProvinceName(String provinceName) {
        return institutionRepository.findAllByProvinceName(provinceName);
    }
    
    public Institution updateInstitution(Long id, Institution updatedInstitution) {
        Institution existing = institutionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Institution not found"));
        
        existing.setName(updatedInstitution.getName());
        existing.setEmail(updatedInstitution.getEmail());
        existing.setStatus(updatedInstitution.getStatus());
        
        return institutionRepository.save(existing);
    }
    
    public void deleteInstitution(Long id) {
        if (!institutionRepository.existsById(id)) {
            throw new RuntimeException("Institution not found");
        }
        institutionRepository.deleteById(id);
    }
}
