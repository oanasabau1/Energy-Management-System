package com.example.monitoring_microservice.service;

import com.example.monitoring_microservice.dto.HourlyConsumptionDTO;
import com.example.monitoring_microservice.entity.HourlyConsumption;
import com.example.monitoring_microservice.mapper.HourlyConsumptionMapper;
import com.example.monitoring_microservice.repository.HourlyConsumptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HourlyConsumptionService {

    @Autowired
    private HourlyConsumptionRepository hourlyConsumptionRepository;

    @Autowired
    private HourlyConsumptionMapper hourlyConsumptionMapper;

    public HourlyConsumptionDTO saveHourlyConsumption(Long userId, Long deviceId, Double maxHourlyConsumption, Double consumption) {
        HourlyConsumptionDTO hourlyConsumptionDTO = hourlyConsumptionMapper.toDTO(userId, deviceId, maxHourlyConsumption, consumption);
        HourlyConsumption hourlyConsumption = hourlyConsumptionMapper.toHourlyConsumption(hourlyConsumptionDTO);
        HourlyConsumption savedHourlyConsumption = hourlyConsumptionRepository.save(hourlyConsumption);
        return hourlyConsumptionMapper.toDTO(savedHourlyConsumption);
    }
}