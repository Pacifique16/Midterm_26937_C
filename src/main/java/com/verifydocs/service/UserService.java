package com.verifydocs.service;

import com.verifydocs.entity.User;
import com.verifydocs.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    public User saveUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("User with email " + user.getEmail() + " already exists");
        }
        return userRepository.save(user);
    }
    
    // Get all users without pagination
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    // Pagination - retrieves users page by page
    public Page<User> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAll(pageable);
    }
    
    // Sorting - retrieves users sorted by a field
    public List<User> getAllUsersSorted(String sortBy) {
        Sort sort = Sort.by(sortBy).ascending();
        return userRepository.findAll(sort);
    }
    
    // Pagination + Sorting combined
    public Page<User> getAllUsersPaginatedAndSorted(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        return userRepository.findAll(pageable);
    }
    
    // Retrieve all users from a given province using province code
    public List<User> getUsersByProvinceCode(String provinceCode) {
        return userRepository.findAllByProvinceCode(provinceCode);
    }
    
    // Retrieve all users from a given province using province name
    public List<User> getUsersByProvinceName(String provinceName) {
        return userRepository.findAllByProvinceName(provinceName);
    }
    
    public User getUserById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));
    }
    
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    public User updateUser(Long id, User updatedUser) {
        User existing = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        existing.setFullName(updatedUser.getFullName());
        existing.setRole(updatedUser.getRole());
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            existing.setPassword(updatedUser.getPassword());
        }
        
        return userRepository.save(existing);
    }
    
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }
}
