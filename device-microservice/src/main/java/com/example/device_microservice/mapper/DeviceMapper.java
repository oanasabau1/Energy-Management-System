package com.example.device_microservice.mapper;

import com.example.device_microservice.dto.DeviceDTO;
import com.example.device_microservice.dto.DeviceRegisterDTO;
import com.example.device_microservice.entity.Device;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DeviceMapper {
       public Device toDevice(DeviceRegisterDTO dto){
        Device device = new Device();
        device.setUserId(dto.getUserId());
        device.setDescription(dto.getDescription());
        device.setAddress(dto.getAddress());
        device.setMaxHourlyConsumption(dto.getMaxHourlyConsumption());
        return device;
    }

    public DeviceDTO toDTO(Device device) {
        DeviceDTO dto = new DeviceDTO();
        dto.setDeviceId(device.getDeviceId());
        dto.setUserId(device.getUserId());
        dto.setDescription(device.getDescription());
        dto.setAddress(device.getAddress());
        dto.setMaxHourlyConsumption(device.getMaxHourlyConsumption());
        return dto;
    }
}
