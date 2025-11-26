package com.ewallet.repository;

import com.ewallet.model.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface TransactionRepository extends MongoRepository<Transaction, String> {
    List<Transaction> findBySenderIdOrReceiverId(String senderId, String receiverId);
}
