package com.ewallet.repository;

import com.ewallet.model.FraudAlert;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface FraudAlertRepository extends MongoRepository<FraudAlert, String> {
    List<FraudAlert> findByUserId(String userId);
}
