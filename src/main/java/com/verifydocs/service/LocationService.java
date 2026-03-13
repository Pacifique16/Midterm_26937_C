package com.verifydocs.service;

import com.verifydocs.entity.Location;
import com.verifydocs.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {
    
    @Autowired
    private LocationRepository locationRepository;
    
    public Location saveLocation(Location location) {
        if (locationRepository.existsByCode(location.getCode())) {
            throw new RuntimeException("Location already exists");
        }
        return locationRepository.save(location);
    }
    
    public Location getLocationByCode(String code) {
        return locationRepository.findByCode(code)
            .orElseThrow(() -> new RuntimeException("Location not found"));
    }
    
    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }
    
    public Location updateLocation(Long id, Location updatedLocation) {
        Location existing = locationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Location not found"));
        
        existing.setName(updatedLocation.getName());
        existing.setLevel(updatedLocation.getLevel());
        if (updatedLocation.getParent() != null) {
            existing.setParent(updatedLocation.getParent());
        }
        
        return locationRepository.save(existing);
    }
    
    public void deleteLocation(Long id) {
        if (!locationRepository.existsById(id)) {
            throw new RuntimeException("Location not found");
        }
        locationRepository.deleteById(id);
    }
}
