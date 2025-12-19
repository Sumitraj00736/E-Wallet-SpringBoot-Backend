package com.ewallet.repository;

import com.ewallet.model.QRPayment;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;
import java.util.List;

public interface QRPaymentRepository extends MongoRepository<QRPayment, String> {

    Optional<QRPayment> findByQrCodeData(String qrCodeData); // ✅ fetch by QR string

    List<QRPayment> findByUserId(String userId); // ✅ fetch all QR for a user
}
