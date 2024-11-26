package com.example.monitoring_microservice.service;

import com.example.monitoring_microservice.dto.MeasurementDTO;
import com.example.monitoring_microservice.entity.Measurement;
import com.example.monitoring_microservice.mapper.MeasurementMapper;
import com.example.monitoring_microservice.repository.MeasurementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MeasurementService {

    @Autowired
    private MeasurementRepository measurementRepository;
    @Autowired
    private MeasurementMapper measurementMapper;

    public MeasurementDTO registerMeasurement(String message) {
        try {
            Measurement measurement = measurementMapper.toMeasurement(message);
            Measurement savedMeasurement = measurementRepository.save(measurement);
            return measurementMapper.toDTO(savedMeasurement);
        } catch (Exception e) {
            throw new RuntimeException("Failed to register measurement", e);
        }
    }

    public List<MeasurementDTO> getAllMeasurementsForDeviceByDay(Long deviceId, LocalDate date) {
        List<Measurement> measurements = measurementRepository.findByDeviceIdAndTimestampBetween(
                deviceId, date.atStartOfDay(), date.atTime(23, 59, 59));
        return measurements.stream().map(measurementMapper::toDTO).toList();
    }
}
