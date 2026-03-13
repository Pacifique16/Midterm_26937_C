package com.verifydocs.controller;

import com.verifydocs.entity.Location;
import com.verifydocs.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
public class LocationController {
    
    @Autowired
    private LocationService locationService;
    
    @PostMapping
    public ResponseEntity<Location> saveLocation(@RequestBody Location location) {
        return ResponseEntity.ok(locationService.saveLocation(location));
    }
    
    @GetMapping
    public ResponseEntity<List<Location>> getAllLocations() {
        return ResponseEntity.ok(locationService.getAllLocations());
    }
    
    @GetMapping("/{code}")
    public ResponseEntity<Location> getLocationByCode(@PathVariable String code) {
        return ResponseEntity.ok(locationService.getLocationByCode(code));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Location> updateLocation(@PathVariable Long id, @RequestBody Location location) {
        return ResponseEntity.ok(locationService.updateLocation(id, location));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLocation(@PathVariable Long id) {
        locationService.deleteLocation(id);
        return ResponseEntity.ok("Location deleted successfully");
    }
}
