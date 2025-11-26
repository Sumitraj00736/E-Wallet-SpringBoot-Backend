package com.ewallet.controller;

import com.ewallet.model.QRPayment;
import com.ewallet.security.JwtUtil;
import com.ewallet.service.QRPaymentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/qr")
public class QRPaymentController {

    @Autowired
    private QRPaymentService qrPaymentService;

    @Autowired
    private JwtUtil jwtUtil; 

    // Request body class for QR generation (optional)
    public static class QRRequest {
        private BigDecimal amount;
        private String purpose;

        public QRRequest() { }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public String getPurpose() {
            return purpose;
        }

        public void setPurpose(String purpose) {
            this.purpose = purpose;
        }
    }

    // Generate QR code
    @PostMapping("/generate")
    public ResponseEntity<?> generateQR(
            @RequestBody Map<String, BigDecimal> body,
            HttpServletRequest request) {

        BigDecimal amount = body.get("amount");

        String tokenHeader = request.getHeader("Authorization");
        if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Missing or invalid Authorization header");
        }
        String token = tokenHeader.substring(7);

        String userId = jwtUtil.getUserIdFromToken(token);

        QRPayment qr = qrPaymentService.generateQR(userId, amount);

        return ResponseEntity.ok(qr);
    }

    // Pay using QR code
    @PostMapping("/pay")
    public ResponseEntity<?> payUsingQR(@RequestParam String qrId, @RequestParam String payerId) {
        boolean success = qrPaymentService.payUsingQR(qrId, payerId);
        if (success)
            return ResponseEntity.ok("Payment successful");
        return ResponseEntity.badRequest().body("Payment failed");
    }

    // Get all QR codes for a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserQRs(@PathVariable String userId) {
        List<QRPayment> qrList = qrPaymentService.getUserQRs(userId);
        return ResponseEntity.ok(qrList);
    }
}
