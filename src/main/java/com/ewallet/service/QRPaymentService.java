package com.ewallet.service;

import com.ewallet.model.QRPayment;
import com.ewallet.model.User;
import com.ewallet.repository.QRPaymentRepository;
import com.ewallet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class QRPaymentService {

    @Autowired
    private QRPaymentRepository qrPaymentRepository;

    @Autowired
    private UserRepository userRepository;

    public QRPayment generateQR( String userId,BigDecimal amount) {
        QRPayment qr = new QRPayment();
        qr.setUserId(userId);
        qr.setAmount(amount);
        qr.setQrCodeData(UUID.randomUUID().toString());
        qr.setUsed(false);
        qr.setCreatedAt(LocalDateTime.now());

        return qrPaymentRepository.save(qr);
    }

    public boolean payUsingQR(String qrCodeData, String userId) {
        QRPayment qrOpt = qrPaymentRepository.findById(qrCodeData).orElse(null);
        System.err.println(qrOpt);
        System.out.println(userId);
        User payer = userRepository.findById(userId).orElse(null);
        System.err.println(payer);
        
        User payee = userRepository.findById(qrOpt.getUserId()).orElse(null);
        System.err.println(payee);

        if (qrOpt != null && !qrOpt.isUsed() && payer != null && payee != null) {
            if (payer.getWalletBalance().compareTo(qrOpt.getAmount()) >= 0) {
                payer.setWalletBalance(payer.getWalletBalance().subtract(qrOpt.getAmount()));
                payee.setWalletBalance(payee.getWalletBalance().add(qrOpt.getAmount()));
                qrOpt.setUsed(true);

                userRepository.save(payer);
                userRepository.save(payee);
                qrPaymentRepository.save(qrOpt);

                return true;
            }
        }
        return false;
    }

    public List<QRPayment> getUserQRs(String userId) {
        return qrPaymentRepository.findByUserId(userId);
    }
}
