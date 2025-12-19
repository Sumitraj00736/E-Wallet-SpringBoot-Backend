package com.ewallet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void notifyUser(String userId, String message) {
        if (message != null) {
            messagingTemplate.convertAndSend(
                    "/topic/notifications/" + userId,
                    message
            );
        }
    }
}
