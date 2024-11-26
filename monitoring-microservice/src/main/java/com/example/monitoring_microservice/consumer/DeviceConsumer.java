package com.example.monitoring_microservice.consumer;

import com.example.monitoring_microservice.service.DeviceService;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeviceConsumer {
    private final DeviceService deviceService;
    private final RabbitAdmin rabbitAdmin;

    @Autowired
    public DeviceConsumer(DeviceService deviceService, RabbitAdmin rabbitAdmin) {
        this.deviceService = deviceService;
        this.rabbitAdmin = rabbitAdmin;
        ensureQueueExists("device-queue");
    }

    @RabbitListener(queues = "device-queue")
    public void receiveMessage(String message) {
        String type = getHttpMethod(message);

        switch (type) {
            case "POST":
                deviceService.registerDevice(message);
                break;
            case "PUT":
                deviceService.updateDevice(message);
                break;
            case "DELETE":
                deviceService.deleteDevice(message);
                break;
            default:
                System.out.println("Unknown message type: " + type);
        }

        System.out.println("Received from RabbitMQ: " + message);
    }

    private String getHttpMethod(String message) {
        String trimmedJson = message.replaceAll("[{}\"]", "");
        String[] keyValuePairs = trimmedJson.split(",");
        return keyValuePairs[0].split(":")[1].trim();
    }

   private void ensureQueueExists(String queueName) {
        if (rabbitAdmin.getQueueProperties(queueName) == null || rabbitAdmin.getQueueProperties(queueName).isEmpty()) {
            System.out.println("Queue " + queueName + " does not exist. Declaring queue.");
            rabbitAdmin.declareQueue(new Queue(queueName));
        } else {
            System.out.println("Queue " + queueName + " already exists.");
        }
    }
}