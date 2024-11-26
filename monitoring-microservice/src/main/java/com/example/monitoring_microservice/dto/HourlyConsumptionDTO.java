package com.example.monitoring_microservice.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class HourlyConsumptionDTO {
    Long deviceId;
    Long userId;
    Double maxHourlyConsumption;
    Double consumption;
}
