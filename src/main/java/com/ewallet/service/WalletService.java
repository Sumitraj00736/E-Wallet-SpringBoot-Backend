package com.ewallet.service;

import com.ewallet.model.Transaction;
import com.ewallet.model.User;
import com.ewallet.repository.TransactionRepository;
import com.ewallet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class WalletService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;  

    public User topUp(String userId, BigDecimal amount) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setWalletBalance(user.getWalletBalance().add(amount));

            // Create transaction
            Transaction tx = new Transaction();
            tx.setSenderId(userId);
            tx.setReceiverId(userId);
            tx.setAmount(amount);
            tx.setType("TOPUP");
            tx.setStatus("SUCCESS");
            tx.setCreatedAt(LocalDateTime.now());
            transactionRepository.save(tx);

            return userRepository.save(user);
        }
        return null;
    }

    public boolean transfer(String senderId, String receiverId, BigDecimal amount) {
        Optional<User> senderOpt = userRepository.findById(senderId);
        Optional<User> receiverOpt = userRepository.findById(receiverId);

        if (senderOpt.isPresent() && receiverOpt.isPresent()) {
            User sender = senderOpt.get();
            User receiver = receiverOpt.get();

            if (sender.getWalletBalance().compareTo(amount) >= 0) {
                sender.setWalletBalance(sender.getWalletBalance().subtract(amount));
                receiver.setWalletBalance(receiver.getWalletBalance().add(amount));

                userRepository.save(sender);
                userRepository.save(receiver);

                // Create transaction
                Transaction tx = new Transaction();
                tx.setSenderId(senderId);
                tx.setReceiverId(receiverId);
                tx.setAmount(amount);
                tx.setType("TRANSFER");
                tx.setStatus("SUCCESS");
                tx.setCreatedAt(LocalDateTime.now());
                transactionRepository.save(tx);

                return true;
            }
        }
        return false;
    }

    public BigDecimal getBalance(String userId) {
        Optional<User> userOpt = userRepository.findById(userId);
    System.err.println(userOpt);
        if (userOpt.isPresent()) {
            System.err.println(userOpt.get().getWalletBalance());
            return userOpt.get().getWalletBalance(); // Make sure User has walletBalance field
        }
        return BigDecimal.ZERO;
    }

    public List<Transaction> getTransactions(String userId) {
        return transactionRepository.findBySenderIdOrReceiverId(userId, userId);
    }
    public boolean verifyUserPassword(String userId, String rawPassword) {
    return userRepository.findById(userId)
            .map(user -> passwordEncoder.matches(rawPassword, user.getPassword()))
            .orElse(false);
}

}
