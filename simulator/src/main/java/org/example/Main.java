package org.example;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        Properties properties = new Properties();
        try (InputStream input = Main.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find config.properties");
                return;
            }
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
            return;
        }

        String deviceId = properties.getProperty("device.deviceId");
        System.out.println("Device ID from properties: " + deviceId);
        String csvPath = "D:\\ds-energy-management\\simulator\\src\\main\\resources\\sensor.csv";

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);

        try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
            channel.queueDeclare("measurements-queue", true, false, false, null);
            System.out.println("Connected to RabbitMQ");

            try (BufferedReader br = new BufferedReader(new FileReader(csvPath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String value = line.trim();
                    LocalDateTime currentDateTime = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SS");
                    String timestamp = currentDateTime.format(formatter);

                    String jsonMessage = String.format("{\"timestamp\": \"%s\", \"device_id\": \"%s\", \"measurement_value\": %s}", timestamp, deviceId, value);
                    channel.basicPublish("", "measurements-queue", null, jsonMessage.getBytes());
                    System.out.println("Sent to RabbitMQ: " + jsonMessage);
                    Thread.sleep(600000); // 10 minutes delay
                }
            } catch (IOException e) {
                System.out.println("File not found");
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.out.println("Connection to RabbitMQ failed");
            e.printStackTrace();
        }
    }
}
