package com.example.chat_microservice.controller;

import com.example.chat_microservice.entity.ChatMessage;
import com.example.chat_microservice.entity.ReadReceipt;
import com.example.chat_microservice.entity.TypingMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.LocalDateTime;

@Controller
@CrossOrigin
public class ChatController {

    @Autowired
    private SimpMessagingTemplate template;

    @MessageMapping("/publish")
    public void handleChatMessage(@Payload ChatMessage message) {
        try {
            message.setTimestamp(LocalDateTime.now());
            System.out.println("Received message: " + message.getMessage() + " from " + message.getSenderUsername()
                    + " to " + message.getReceiverUsername() + " at " + message.getTimestamp());

            template.convertAndSend("/topic/chat/" + message.getSenderId() + "/" + message.getReceiverId(), message);
            template.convertAndSend("/topic/notification/" + message.getReceiverId(), message);
        } catch (Exception e) {
            System.err.println("Error handling chat message: " + e.getMessage());
        }
    }

    @MessageMapping("/typing")
    public void handleTypingEvent(@Payload TypingMessage message) {
        try {
            System.out.println("Received typing event: " + message.getSenderUsername() + " is typing to "
                    + message.getReceiverUsername());
            template.convertAndSend("/topic/typing/" + message.getSenderId() + "/" + message.getReceiverId(), message);
        } catch (Exception e) {
            System.err.println("Error handling typing event: " + e.getMessage());
        }
    }

    @MessageMapping("/read")
    public void handleReadMessage(@Payload ReadReceipt receipt) {
        try {
            System.out.println("Read receipt received for message ID: " + receipt.getMessageId());
            receipt.setTimestamp(LocalDateTime.now());
            template.convertAndSend("/topic/read/" + receipt.getSenderId(), receipt);
            System.out.println("Read receipt sent to sender ID: " + receipt.getSenderId());
        } catch (Exception e) {
            System.err.println("Error handling read receipt: " + e.getMessage());
        }
    }
}
