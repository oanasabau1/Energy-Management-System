package com.example.monitoring_microservice.controller;

import com.example.monitoring_microservice.authorization.JwtTokenValidator;
import com.example.monitoring_microservice.dto.MeasurementDTO;
import com.example.monitoring_microservice.service.MeasurementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin
@RestController
public class MonitoringController {
    @Autowired
    private MeasurementService measurementService;
    @Autowired
    private JwtTokenValidator jwtTokenValidator;

    @GetMapping("/device/{id}/measurements/{date}")
    ResponseEntity<List<MeasurementDTO>> getAllMeasurementsForDeviceByDate(@PathVariable("id") Long deviceId,
                                                                           @PathVariable("date") LocalDate date,
                                                                           @RequestHeader("Authorization") String token) {
        if (!jwtTokenValidator.validateJwtToken(token)) {
            return ResponseEntity.status(401).build();
        }
        List<MeasurementDTO> measurements = measurementService.getAllMeasurementsForDeviceByDay(deviceId, date);
        return ResponseEntity.ok(measurements);
    }
}
