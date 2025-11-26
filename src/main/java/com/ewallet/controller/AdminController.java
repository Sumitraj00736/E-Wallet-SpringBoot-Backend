package com.ewallet.controller;

import com.ewallet.model.FraudAlert;
import com.ewallet.model.Transaction;
import com.ewallet.model.User;
import com.ewallet.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    // Get all users
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    // Get all transactions
    @GetMapping("/transactions")
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        return ResponseEntity.ok(adminService.getAllTransactions());
    }

    // Create fraud alert
    @PostMapping("/fraud-alert")
    public ResponseEntity<FraudAlert> createFraudAlert(@RequestParam String userId, @RequestParam String message) {
        return ResponseEntity.ok(adminService.createFraudAlert(userId, message));
    }

    // Block a user
    @PostMapping("/block-user")
    public ResponseEntity<User> blockUser(@RequestParam String userId) {
        return ResponseEntity.ok(adminService.blockUser(userId));
    }
}
