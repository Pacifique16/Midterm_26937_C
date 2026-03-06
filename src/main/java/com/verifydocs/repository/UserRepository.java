package com.verifydocs.repository;

import com.verifydocs.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // existBy() method - checks if user exists by email
    boolean existsByEmail(String email);
    
    User findByEmail(String email);
    
    // Retrieve all users from a given province using province code
    @Query("SELECT u FROM User u WHERE u.institution.province.code = :provinceCode")
    List<User> findAllByProvinceCode(@Param("provinceCode") String provinceCode);
    
    // Retrieve all users from a given province using province name
    @Query("SELECT u FROM User u WHERE u.institution.province.name = :provinceName")
    List<User> findAllByProvinceName(@Param("provinceName") String provinceName);
    
    // Pagination support for users
    Page<User> findAll(Pageable pageable);
}
