package com.example.chat_microservice.entity;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TypingMessage {
    private Long senderId;
    private String senderUsername;
    private Long receiverId;
    private String receiverUsername;
    private boolean isTyping;
}
