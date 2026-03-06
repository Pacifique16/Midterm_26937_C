package com.verifydocs.repository;

import com.verifydocs.entity.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    
    Document findByVerificationCode(String verificationCode);
    
    // existBy() method - checks if document exists by verification code
    boolean existsByVerificationCode(String verificationCode);
    
    // Pagination and Sorting support
    Page<Document> findAll(Pageable pageable);
    
    Page<Document> findByInstitutionId(Long institutionId, Pageable pageable);
    
    List<Document> findByInstitutionId(Long institutionId);
}
