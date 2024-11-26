package com.example.device_microservice.publisher;

public interface RabbitMQMessageSender {
    void sendMessage(Long deviceId, Long userId, Double maxHourlyConsumption);
}