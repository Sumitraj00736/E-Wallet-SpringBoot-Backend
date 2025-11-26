package com.ewallet.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = "qr_payments")
public class QRPayment {

    @Id
    private String id;
    private String userId;
    private String qrCodeData;
    private BigDecimal amount;
    private boolean used = false;
    private LocalDateTime createdAt = LocalDateTime.now();

    // Constructors
    public QRPayment() {}

    public QRPayment(String id, String userId, String qrCodeData, BigDecimal amount, boolean used, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.qrCodeData = qrCodeData;
        this.amount = amount;
        this.used = used;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getQrCodeData() { return qrCodeData; }
    public void setQrCodeData(String qrCodeData) { this.qrCodeData = qrCodeData; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public boolean isUsed() { return used; }
    public void setUsed(boolean used) { this.used = used; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
