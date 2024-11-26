package com.example.monitoring_microservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class MeasurementDTO {
    Long measurementId;
    Long deviceId;
    Double value;
    LocalDateTime timestamp;
}
