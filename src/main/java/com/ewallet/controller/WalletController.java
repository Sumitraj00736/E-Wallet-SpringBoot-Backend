package com.ewallet.controller;

import com.ewallet.model.Transaction;
import com.ewallet.model.User;
import com.ewallet.service.WalletService;
import com.ewallet.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @Autowired
    private JwtUtil jwtUtil;

    // Top-up wallet
    @PostMapping("/topup")
    public ResponseEntity<?> topUp(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, Object> payload) {

        String token = authHeader.substring(7);
        String userId = jwtUtil.getUserIdFromToken(token);

        BigDecimal amount = new BigDecimal(payload.get("amount").toString());
        User user = walletService.topUp(userId, amount);
        if (user != null)
            return ResponseEntity.ok(user);

        return ResponseEntity.badRequest().body("User not found");
    }

    // Transfer money
    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, Object> payload) {

        String token = authHeader.substring(7);
        String senderId = jwtUtil.getUserIdFromToken(token);

        String receiverId = payload.get("receiverId").toString();
        BigDecimal amount = new BigDecimal(payload.get("amount").toString());

        boolean success = walletService.transfer(senderId, receiverId, amount);
        if (success)
            return ResponseEntity.ok("Transfer successful");

        return ResponseEntity.badRequest().body("Insufficient balance or invalid user");
    }

    // Get wallet balance
    @GetMapping("/balance")
    public ResponseEntity<?> getBalance(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        String userId = jwtUtil.getUserIdFromToken(token);
        return ResponseEntity.ok(walletService.getBalance(userId));
    }

    // Get transactions
    @GetMapping("/transactions")
    public ResponseEntity<?> getTransactions(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        String userId = jwtUtil.getUserIdFromToken(token);

        List<Transaction> transactions = walletService.getTransactions(userId);
        return ResponseEntity.ok(transactions);
    }
}
