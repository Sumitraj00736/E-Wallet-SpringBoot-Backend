package com.ewallet.service;

import com.ewallet.model.FraudAlert;
import com.ewallet.model.Transaction;
import com.ewallet.model.User;
import com.ewallet.repository.FraudAlertRepository;
import com.ewallet.repository.TransactionRepository;
import com.ewallet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private FraudAlertRepository fraudAlertRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public FraudAlert createFraudAlert(String userId, String message) {
        FraudAlert alert = new FraudAlert();
        alert.setUserId(userId);
        alert.setMessage(message);
        return fraudAlertRepository.save(alert);
    }

    public User blockUser(String userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            user.setActive(false);
            return userRepository.save(user);
        }
        return null;
    }
}
