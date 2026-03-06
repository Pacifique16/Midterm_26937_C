package com.verifydocs.repository;

import com.verifydocs.entity.Institution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstitutionRepository extends JpaRepository<Institution, Long> {
    
    // existBy() method - checks if institution exists by email
    boolean existsByEmail(String email);
    
    Institution findByEmail(String email);
}
