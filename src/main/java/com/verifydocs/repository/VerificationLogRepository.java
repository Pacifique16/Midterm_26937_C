package com.verifydocs.repository;

import com.verifydocs.entity.VerificationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VerificationLogRepository extends JpaRepository<VerificationLog, Long> {
    
    List<VerificationLog> findByDocumentId(Long documentId);
}
