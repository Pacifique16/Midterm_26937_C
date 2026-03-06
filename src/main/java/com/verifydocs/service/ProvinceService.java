package com.verifydocs.service;

import com.verifydocs.entity.Province;
import com.verifydocs.repository.ProvinceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProvinceService {
    
    @Autowired
    private ProvinceRepository provinceRepository;
    
    // Save Location (Province) - stores province data with code and name
    public Province saveProvince(Province province) {
        // Check if province already exists using existBy() method
        if (provinceRepository.existsByCode(province.getCode())) {
            throw new RuntimeException("Province with code " + province.getCode() + " already exists");
        }
        return provinceRepository.save(province);
    }
    
    public List<Province> getAllProvinces() {
        return provinceRepository.findAll();
    }
    
    public Province getProvinceById(Long id) {
        return provinceRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Province not found"));
    }
    
    public Province getProvinceByCode(String code) {
        return provinceRepository.findByCode(code);
    }
    
    public boolean existsByCode(String code) {
        return provinceRepository.existsByCode(code);
    }
}
