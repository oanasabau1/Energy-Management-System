package com.example.monitoring_microservice.controller;

import com.example.monitoring_microservice.dto.MeasurementDTO;
import com.example.monitoring_microservice.service.MeasurementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin
@RestController
public class MonitoringController {
    @Autowired
    private MeasurementService measurementService;

    @GetMapping("/device/{id}/measurements/{date}")
    ResponseEntity<List<MeasurementDTO>> getAllMeasurementsForDeviceByDate(@PathVariable("id") Long deviceId, @PathVariable("date") LocalDate date) {
        List<MeasurementDTO> measurements = measurementService.getAllMeasurementsForDeviceByDay(deviceId, date);
        return ResponseEntity.ok(measurements);
    }
}
