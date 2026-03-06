package com.verifydocs.repository;

import com.verifydocs.entity.InstitutionProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstitutionProfileRepository extends JpaRepository<InstitutionProfile, Long> {
    
    InstitutionProfile findByInstitutionId(Long institutionId);
}
