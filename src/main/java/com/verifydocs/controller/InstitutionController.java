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
            @RequestParam String provinceCode) {
        Institution saved = institutionService.saveInstitution(institution, provinceCode);
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
    
    @GetMapping
    public ResponseEntity<List<Institution>> getAllInstitutions() {
        return ResponseEntity.ok(institutionService.getAllInstitutions());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Institution> getInstitutionById(@PathVariable Long id) {
        return ResponseEntity.ok(institutionService.getInstitutionById(id));
    }
}
