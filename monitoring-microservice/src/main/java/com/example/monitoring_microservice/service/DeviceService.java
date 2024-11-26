package com.example.monitoring_microservice.service;

import com.example.monitoring_microservice.dto.DeviceDTO;
import com.example.monitoring_microservice.entity.Device;
import com.example.monitoring_microservice.mapper.DeviceMapper;
import com.example.monitoring_microservice.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceService {

    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private DeviceMapper deviceMapper;

    public DeviceDTO registerDevice(String message) {
        DeviceDTO deviceDTO = parseDeviceMessage(message);
        Device savedDevice = deviceRepository.save(deviceMapper.toDevice(deviceDTO));
        return deviceMapper.toDTO(savedDevice);
    }

    public DeviceDTO updateDevice(String message) {
        DeviceDTO deviceDTO = parseDeviceMessage(message);
        Device updatedDevice = deviceRepository.save(deviceMapper.toDevice(deviceDTO));
        return deviceMapper.toDTO(updatedDevice);
    }

    public void deleteDevice(String message) {
        Long deviceId = parseDeviceIdFromMessage(message);
        deviceRepository.deleteById(deviceId);
    }


    private DeviceDTO parseDeviceMessage(String message) {
        String trimmedJson = message.replaceAll("[{}\"]", "");
        String[] keyValuePairs = trimmedJson.split(",");
        Long deviceId = Long.valueOf(keyValuePairs[1].split(":")[1].trim());
        Long userId = Long.valueOf(keyValuePairs[2].split(":")[1].trim());
        Double maxHourlyConsumption = Double.valueOf(keyValuePairs[3].split(":")[1].trim());
        return deviceMapper.toDTO(deviceId, userId, maxHourlyConsumption);
    }

    private Long parseDeviceIdFromMessage(String message) {
        String trimmedJson = message.replaceAll("[{}\"]", "");
        String[] keyValuePairs = trimmedJson.split(",");
        return Long.valueOf(keyValuePairs[1].split(":")[1].trim());
    }
}