package com.example.monitoring_microservice.mapper;

import com.example.monitoring_microservice.dto.HourlyConsumptionDTO;
import com.example.monitoring_microservice.entity.HourlyConsumption;
import org.springframework.stereotype.Component;

@Component
public class HourlyConsumptionMapper {
    public HourlyConsumptionDTO toDTO(HourlyConsumption hourlyConsumption) {
        HourlyConsumptionDTO dto = new HourlyConsumptionDTO();
        dto.setDeviceId(hourlyConsumption.getDeviceId());
        dto.setUserId(hourlyConsumption.getUserId());
        dto.setMaxHourlyConsumption(hourlyConsumption.getMaxHourlyConsumption());
        dto.setConsumption(hourlyConsumption.getConsumption());
        return dto;
    }

    public HourlyConsumptionDTO toDTO(Long deviceId, Long userId, Double maxHourlyConsumption, Double totalConsumption) {
        HourlyConsumptionDTO hourlyConsumptionDTO = new HourlyConsumptionDTO();
        hourlyConsumptionDTO.setDeviceId(deviceId);
        hourlyConsumptionDTO.setUserId(userId);
        hourlyConsumptionDTO.setMaxHourlyConsumption(maxHourlyConsumption);
        hourlyConsumptionDTO.setConsumption(totalConsumption);
        return hourlyConsumptionDTO;
    }

    public HourlyConsumption toHourlyConsumption(HourlyConsumptionDTO dto) {
        HourlyConsumption hourlyConsumption = new HourlyConsumption();
        hourlyConsumption.setDeviceId(dto.getDeviceId());
        hourlyConsumption.setUserId(dto.getUserId());
        hourlyConsumption.setMaxHourlyConsumption(dto.getMaxHourlyConsumption());
        hourlyConsumption.setConsumption(dto.getConsumption());
        return hourlyConsumption;
    }
}
