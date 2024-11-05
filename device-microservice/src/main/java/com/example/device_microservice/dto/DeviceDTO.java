package com.example.device_microservice.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DeviceDTO {
    Long deviceId;
    Long userId;
    String description;
    String address;
    Double maxHourlyConsumption;
}
