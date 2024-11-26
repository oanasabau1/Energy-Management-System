package com.example.device_microservice.publisher;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.stereotype.Component;

@Component
public class DeleteRequestSender implements RabbitMQMessageSender {

    @Override
    public void sendMessage(Long deviceId, Long userId, Double maxHourlyConsumption) {
        send("DELETE", deviceId, null, null);
    }

    private void send(String type, Long deviceId, Long userId, Double maxHourlyConsumption) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("rabbitmq");
        try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
            channel.queueDeclare("device-queue", true, false, false, null);

            StringBuilder sb = new StringBuilder();
            sb.append("{");
            sb.append("\"type\": \"" + type + "\",");
            sb.append("\"device_id\": \"" + deviceId + "\"");
            sb.append("}");

            channel.basicPublish("", "device-queue", null, sb.toString().getBytes());
            System.out.println("Sent to RabbitMQ: " + sb.toString());

        } catch (Exception e) {
            System.out.println("Connection to RabbitMQ failed");
            e.printStackTrace();
        }
    }
}