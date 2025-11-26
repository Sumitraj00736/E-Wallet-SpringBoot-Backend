package com.ewallet.repository;

import com.ewallet.model.QRPayment;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface QRPaymentRepository extends MongoRepository<QRPayment, String> {
    List<QRPayment> findByUserId(String userId);
}
