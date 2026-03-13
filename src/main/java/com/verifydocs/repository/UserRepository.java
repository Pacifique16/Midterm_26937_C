package com.verifydocs.repository;

import com.verifydocs.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    
    User findByEmail(String email);
    
    @Query("SELECT u FROM User u " +
           "JOIN u.institution i " +
           "JOIN i.location v " +
           "JOIN v.parent c " +
           "JOIN c.parent s " +
           "JOIN s.parent d " +
           "JOIN d.parent p " +
           "WHERE p.code = :provinceCode AND p.level = 'PROVINCE'")
    List<User> findAllByProvinceCode(@Param("provinceCode") String provinceCode);
    
    @Query("SELECT u FROM User u " +
           "JOIN u.institution i " +
           "JOIN i.location v " +
           "JOIN v.parent c " +
           "JOIN c.parent s " +
           "JOIN s.parent d " +
           "JOIN d.parent p " +
           "WHERE p.name = :provinceName AND p.level = 'PROVINCE'")
    List<User> findAllByProvinceName(@Param("provinceName") String provinceName);
}
