package com.verifydocs.repository;

import com.verifydocs.entity.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProvinceRepository extends JpaRepository<Province, Long> {
    
    // existBy() method - checks if province exists by code
    boolean existsByCode(String code);
    
    // existBy() method - checks if province exists by name
    boolean existsByName(String name);
    
    Province findByCode(String code);
    Province findByName(String name);
}
