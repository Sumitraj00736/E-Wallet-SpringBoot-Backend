package com.ewallet.service;

import com.ewallet.model.QRPayment;
import com.ewallet.model.User;
import com.ewallet.repository.QRPaymentRepository;
import com.ewallet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class QRPaymentService {

    @Autowired
    private QRPaymentRepository qrPaymentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public QRPayment generateQR(String userId, BigDecimal amount) {
        QRPayment qr = new QRPayment();
        qr.setUserId(userId);
        qr.setAmount(amount);
        qr.setQrCodeData(UUID.randomUUID().toString());
        qr.setUsed(false);
        qr.setCreatedAt(LocalDateTime.now());
        return qrPaymentRepository.save(qr);
    }

@Transactional
public boolean payUsingQR(String qrCodeData, String payerId) {
    QRPayment qr = qrPaymentRepository.findByQrCodeData(qrCodeData).orElse(null);
    if (qr == null || qr.isUsed()) return false;

    User payer = userRepository.findById(payerId).orElse(null);
    User payee = userRepository.findById(qr.getUserId()).orElse(null);
    if (payer == null || payee == null || payer.getId().equals(payee.getId())) return false;
    if (payer.getWalletBalance().compareTo(qr.getAmount()) < 0) return false;

    // Perform transaction
    payer.setWalletBalance(payer.getWalletBalance().subtract(qr.getAmount()));
    payee.setWalletBalance(payee.getWalletBalance().add(qr.getAmount()));
    qr.setUsed(true);

    userRepository.save(payer);
    userRepository.save(payee);
    qrPaymentRepository.save(qr);

    messagingTemplate.convertAndSend(
        "/topic/qr/" + qr.getQrCodeData(),
        Map.of(
            "status", "PAID",
            "amount", qr.getAmount(),
            "payer", payer.getName()
        )
    );

    return true;
}


    public List<QRPayment> getUserQRs(String userId) {
        return qrPaymentRepository.findByUserId(userId);
    }
}
