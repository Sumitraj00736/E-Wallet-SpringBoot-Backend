package com.ewallet.controller;

import com.ewallet.model.QRPayment;
import com.ewallet.security.JwtUtil;
import com.ewallet.service.QRPaymentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

        public QRRequest() {
        }

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
    public ResponseEntity<?> payUsingQR(@RequestBody Map<String, String> payload) {
        try {
            // 1️⃣ Validate request payload
            if (payload == null || !payload.containsKey("qrCodeData") || !payload.containsKey("payerId")) {
                return ResponseEntity
                        .badRequest()
                        .body(Map.of("error", "Missing qrCodeData or payerId in request"));
            }

            String qrCodeData = payload.get("qrCodeData");
            String payerId = payload.get("payerId");

            if (qrCodeData.isBlank() || payerId.isBlank()) {
                return ResponseEntity
                        .badRequest()
                        .body(Map.of("error", "qrCodeData or payerId cannot be empty"));
            }

            // 2️⃣ Attempt payment
            boolean success = qrPaymentService.payUsingQR(qrCodeData, payerId);

            if (success) {
                return ResponseEntity.ok(Map.of(
                        "message", "Payment successful",
                        "qrCodeData", qrCodeData,
                        "payerId", payerId));
            } else {
                System.err.println("Payment failed for QR code: " + qrCodeData + " by payer: " + payerId);
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error",
                                "Payment failed. Possible reasons: insufficient balance, QR already used, invalid QR or payer."));
            }

        } catch (Exception e) {
            // 3️⃣ Catch unexpected errors
            System.err.println("Error processing payUsingQR: " + e.getMessage());
            e.printStackTrace();

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Internal server error: " + e.getMessage()));
        }
    }

    // Get all QR codes for a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserQRs(@PathVariable String userId) {
        List<QRPayment> qrList = qrPaymentService.getUserQRs(userId);
        return ResponseEntity.ok(qrList);
    }
}
