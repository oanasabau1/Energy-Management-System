package com.example.monitoring_microservice.consumer;

import com.example.monitoring_microservice.entity.Device;
import com.example.monitoring_microservice.entity.Measurement;
import com.example.monitoring_microservice.service.HourlyConsumptionService;
import com.example.monitoring_microservice.repository.DeviceRepository;
import com.example.monitoring_microservice.repository.MeasurementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class HourlyConsumptionConsumer {

    @Autowired
    private SimpMessagingTemplate template;
    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private MeasurementRepository measurementRepository;
    @Autowired
    private HourlyConsumptionService hourlyConsumptionService;

    @Scheduled(fixedRate = 3600000) // 1 hour
    public void checkAndNotifyExcessConsumption() {
         List<Device> devices = deviceRepository.findAll();
         for (Device device : devices) {
             ArrayList<Measurement> hourlyMeasurements = measurementRepository.findTop6ByDeviceIdOrderByTimestampDesc(device.getDeviceId()); // to get the last measurements for a device that were recorded in the last hour given the fact that the measurements are recorded every 10 minutes
             Double totalConsumption = hourlyMeasurements.stream()
                     .mapToDouble(Measurement::getValue)
                     .sum();
             System.out.println("Total consumption for device " + device.getDeviceId() + ": " + totalConsumption);
             hourlyConsumptionService.saveHourlyConsumption(
                     device.getDeviceId(),
                     device.getUserId(),
                     device.getMaxHourlyConsumption(),
                     totalConsumption
             );
             if (totalConsumption > device.getMaxHourlyConsumption()) {
                 String notification = String.format(
                         "{\"deviceId\":\"%s\",\"userId\":\"%s\",\"maxHourlyConsumption\":%.2f,\"totalConsumption\":%.2f}",
                         device.getDeviceId(), device.getUserId(), device.getMaxHourlyConsumption(), totalConsumption
                 );
                 // Send the notification to a specific topic for the device (using the device ID in the topic path)
                 template.convertAndSend("/topic/notifications/" + device.getDeviceId(), notification);
                 System.out.println("Sent to Socket: " + notification);
             }
         }
     }
}


