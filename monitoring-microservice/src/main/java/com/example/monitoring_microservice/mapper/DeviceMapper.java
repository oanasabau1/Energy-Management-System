package com.example.monitoring_microservice.mapper;

import com.example.monitoring_microservice.dto.DeviceDTO;
import com.example.monitoring_microservice.entity.Device;
import org.springframework.stereotype.Component;

@Component
public class DeviceMapper {

    public DeviceDTO toDTO(Long deviceId, Long userId, Double maxHourlyConsumption) {
        DeviceDTO deviceDTO = new DeviceDTO();
        deviceDTO.setDeviceId(deviceId);
        deviceDTO.setUserId(userId);
        deviceDTO.setMaxHourlyConsumption(maxHourlyConsumption);
        return deviceDTO;
    }

        public DeviceDTO toDTO(Device device) {
        DeviceDTO deviceDTO = new DeviceDTO();
        deviceDTO.setDeviceId(device.getDeviceId());
        deviceDTO.setUserId(device.getUserId());
        deviceDTO.setMaxHourlyConsumption(device.getMaxHourlyConsumption());
        return deviceDTO;
    }

    public Device toDevice(DeviceDTO deviceDTO) {
        Device device = new Device();
        device.setDeviceId(deviceDTO.getDeviceId());
        device.setUserId(deviceDTO.getUserId());
        device.setMaxHourlyConsumption(deviceDTO.getMaxHourlyConsumption());
        return device;
    }
}
