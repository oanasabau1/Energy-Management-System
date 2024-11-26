package com.example.monitoring_microservice.consumer;

import com.example.monitoring_microservice.service.MeasurementService;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MeasurementConsumer {
    private final MeasurementService measurementService;
    private final RabbitAdmin rabbitAdmin;

    @Autowired
    public MeasurementConsumer(MeasurementService measurementService, RabbitAdmin rabbitAdmin) {
        this.measurementService = measurementService;
        this.rabbitAdmin = rabbitAdmin;
        ensureQueueExists("measurements-queue");
    }

    @RabbitListener(queues = "measurements-queue")
    public void receiveMessage(String message) {
        System.out.println("Received message from RabbitMQ: " + message);
        try {
            measurementService.registerMeasurement(message);
        } catch (Exception e) {
            System.err.println("Error processing message from RabbitMQ: " + message);
            e.printStackTrace();
        }
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
