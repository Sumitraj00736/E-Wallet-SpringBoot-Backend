package com.ewallet.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;

@Document(collection = "users")
public class User {

    @Id
    private String id;
    private String name;
    private String email;
    private String password;
    private String kycDocument;
    private boolean active = true;
    private BigDecimal walletBalance = BigDecimal.ZERO;
    private String role = "USER";

    // Constructors
    public User() {}

    public User(String id, String name, String email, String password, String kycDocument, boolean active, BigDecimal walletBalance, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.kycDocument = kycDocument;
        this.active = active;
        this.walletBalance = walletBalance;
        this.role = role;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getKycDocument() { return kycDocument; }
    public void setKycDocument(String kycDocument) { this.kycDocument = kycDocument; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public BigDecimal getWalletBalance() { return walletBalance; }
    public void setWalletBalance(BigDecimal walletBalance) { this.walletBalance = walletBalance; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
