package com.example.chat_microservice.entity;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReadReceipt {
    private String messageId;
    private String senderId;
    private String receiverId;
    private LocalDateTime timestamp;
}
