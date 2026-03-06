package com.verifydocs.controller;

import com.verifydocs.entity.User;
import com.verifydocs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User saved = userService.saveUser(user);
        return ResponseEntity.ok(saved);
    }
    
    // Pagination endpoint
    @GetMapping("/paginated")
    public ResponseEntity<Page<User>> getUsersPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userService.getAllUsers(page, size));
    }
    
    // Sorting endpoint
    @GetMapping("/sorted")
    public ResponseEntity<List<User>> getUsersSorted(
            @RequestParam(defaultValue = "id") String sortBy) {
        return ResponseEntity.ok(userService.getAllUsersSorted(sortBy));
    }
    
    // Pagination + Sorting combined
    @GetMapping("/paginated-sorted")
    public ResponseEntity<Page<User>> getUsersPaginatedAndSorted(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        return ResponseEntity.ok(userService.getAllUsersPaginatedAndSorted(page, size, sortBy));
    }
    
    // Retrieve all users from a given province using province code
    @GetMapping("/province/code/{provinceCode}")
    public ResponseEntity<List<User>> getUsersByProvinceCode(@PathVariable String provinceCode) {
        return ResponseEntity.ok(userService.getUsersByProvinceCode(provinceCode));
    }
    
    // Retrieve all users from a given province using province name
    @GetMapping("/province/name/{provinceName}")
    public ResponseEntity<List<User>> getUsersByProvinceName(@PathVariable String provinceName) {
        return ResponseEntity.ok(userService.getUsersByProvinceName(provinceName));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }
}
