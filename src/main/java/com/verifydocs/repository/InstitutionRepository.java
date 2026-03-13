package com.verifydocs.repository;

import com.verifydocs.entity.Institution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InstitutionRepository extends JpaRepository<Institution, Long> {
    boolean existsByEmail(String email);
    
    @Query("SELECT i FROM Institution i WHERE i.location.name = :locationName AND i.location.level = 'VILLAGE'")
    List<Institution> findAllByVillageName(@Param("locationName") String locationName);
    
    @Query("SELECT i FROM Institution i " +
           "JOIN i.location v " +
           "JOIN v.parent c " +
           "WHERE c.name = :locationName AND c.level = 'CELL'")
    List<Institution> findAllByCellName(@Param("locationName") String locationName);
    
    @Query("SELECT i FROM Institution i " +
           "JOIN i.location v " +
           "JOIN v.parent c " +
           "JOIN c.parent s " +
           "WHERE s.name = :locationName AND s.level = 'SECTOR'")
    List<Institution> findAllBySectorName(@Param("locationName") String locationName);
    
    @Query("SELECT i FROM Institution i " +
           "JOIN i.location v " +
           "JOIN v.parent c " +
           "JOIN c.parent s " +
           "JOIN s.parent d " +
           "WHERE d.name = :locationName AND d.level = 'DISTRICT'")
    List<Institution> findAllByDistrictName(@Param("locationName") String locationName);
    
    @Query("SELECT i FROM Institution i " +
           "JOIN i.location v " +
           "JOIN v.parent c " +
           "JOIN c.parent s " +
           "JOIN s.parent d " +
           "JOIN d.parent p " +
           "WHERE p.name = :locationName AND p.level = 'PROVINCE'")
    List<Institution> findAllByProvinceName(@Param("locationName") String locationName);
}
