package com.verifydocs.repository;

import com.verifydocs.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    boolean existsByCode(String code);
    boolean existsByName(String name);
    Optional<Location> findByCode(String code);
    Optional<Location> findByNameAndLevel(String name, String level);
}
