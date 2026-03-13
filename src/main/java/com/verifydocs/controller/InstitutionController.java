package com.verifydocs.controller;

import com.verifydocs.entity.Institution;
import com.verifydocs.entity.InstitutionProfile;
import com.verifydocs.service.InstitutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/institutions")
public class InstitutionController {
    
    @Autowired
    private InstitutionService institutionService;
    
    @PostMapping
    public ResponseEntity<Institution> createInstitution(
            @RequestBody Institution institution,
            @RequestParam(required = true) String villageCode) {
        Institution saved = institutionService.saveInstitution(institution, villageCode);
        return ResponseEntity.ok(saved);
    }
    
    // One-to-One relationship: Create profile for institution
    @PostMapping("/{institutionId}/profile")
    public ResponseEntity<InstitutionProfile> createProfile(
            @PathVariable Long institutionId,
            @RequestBody InstitutionProfile profile) {
        InstitutionProfile saved = institutionService.createProfile(institutionId, profile);
        return ResponseEntity.ok(saved);
    }
    
    // Update existing profile
    @PutMapping("/{institutionId}/profile")
    public ResponseEntity<InstitutionProfile> updateProfile(
            @PathVariable Long institutionId,
            @RequestBody InstitutionProfile profile) {
        InstitutionProfile updated = institutionService.updateProfile(institutionId, profile);
        return ResponseEntity.ok(updated);
    }
    
    // Delete profile
    @DeleteMapping("/{institutionId}/profile")
    public ResponseEntity<String> deleteProfile(@PathVariable Long institutionId) {
        institutionService.deleteProfile(institutionId);
        return ResponseEntity.ok("Institution profile deleted successfully");
    }
    
    @GetMapping
    public ResponseEntity<List<Institution>> getAllInstitutions() {
        return ResponseEntity.ok(institutionService.getAllInstitutions());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Institution> getInstitutionById(@PathVariable Long id) {
        return ResponseEntity.ok(institutionService.getInstitutionById(id));
    }
    
    // Retrieve all institutions from a given village
    @GetMapping("/location/village/{village}")
    public ResponseEntity<?> getInstitutionsByVillage(@PathVariable String village) {
        List<Institution> institutions = institutionService.getInstitutionsByVillage(village);
        if (institutions.isEmpty()) {
            return ResponseEntity.ok("No institutions registered in our system for village: " + village);
        }
        return ResponseEntity.ok(institutions);
    }
    
    // Retrieve all institutions from a given cell
    @GetMapping("/location/cell/{cell}")
    public ResponseEntity<?> getInstitutionsByCell(@PathVariable String cell) {
        List<Institution> institutions = institutionService.getInstitutionsByCell(cell);
        if (institutions.isEmpty()) {
            return ResponseEntity.ok("No institutions registered in our system for cell: " + cell);
        }
        return ResponseEntity.ok(institutions);
    }
    
    // Retrieve all institutions from a given sector
    @GetMapping("/location/sector/{sector}")
    public ResponseEntity<?> getInstitutionsBySector(@PathVariable String sector) {
        List<Institution> institutions = institutionService.getInstitutionsBySector(sector);
        if (institutions.isEmpty()) {
            return ResponseEntity.ok("No institutions registered in our system for sector: " + sector);
        }
        return ResponseEntity.ok(institutions);
    }
    
    // Retrieve all institutions from a given district
    @GetMapping("/location/district/{district}")
    public ResponseEntity<?> getInstitutionsByDistrict(@PathVariable String district) {
        List<Institution> institutions = institutionService.getInstitutionsByDistrict(district);
        if (institutions.isEmpty()) {
            return ResponseEntity.ok("No institutions registered in our system for district: " + district);
        }
        return ResponseEntity.ok(institutions);
    }
    
    // Retrieve all institutions from a given province
    @GetMapping("/location/province/{provinceName}")
    public ResponseEntity<?> getInstitutionsByProvinceName(@PathVariable String provinceName) {
        List<Institution> institutions = institutionService.getInstitutionsByProvinceName(provinceName);
        if (institutions.isEmpty()) {
            return ResponseEntity.ok("No institutions registered in our system for province: " + provinceName);
        }
        return ResponseEntity.ok(institutions);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Institution> updateInstitution(@PathVariable Long id, @RequestBody Institution institution) {
        return ResponseEntity.ok(institutionService.updateInstitution(id, institution));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteInstitution(@PathVariable Long id) {
        institutionService.deleteInstitution(id);
        return ResponseEntity.ok("Institution deleted successfully");
    }
}
