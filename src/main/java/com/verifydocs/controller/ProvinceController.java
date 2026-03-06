package com.verifydocs.controller;

import com.verifydocs.entity.Province;
import com.verifydocs.service.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/provinces")
public class ProvinceController {
    
    @Autowired
    private ProvinceService provinceService;
    
    // Save Location (Province)
    @PostMapping
    public ResponseEntity<Province> createProvince(@RequestBody Province province) {
        Province saved = provinceService.saveProvince(province);
        return ResponseEntity.ok(saved);
    }
    
    @GetMapping
    public ResponseEntity<List<Province>> getAllProvinces() {
        return ResponseEntity.ok(provinceService.getAllProvinces());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Province> getProvinceById(@PathVariable Long id) {
        return ResponseEntity.ok(provinceService.getProvinceById(id));
    }
    
    @GetMapping("/code/{code}")
    public ResponseEntity<Province> getProvinceByCode(@PathVariable String code) {
        return ResponseEntity.ok(provinceService.getProvinceByCode(code));
    }
    
    // Check if province exists using existBy() method
    @GetMapping("/exists/{code}")
    public ResponseEntity<Boolean> checkProvinceExists(@PathVariable String code) {
        return ResponseEntity.ok(provinceService.existsByCode(code));
    }
}
