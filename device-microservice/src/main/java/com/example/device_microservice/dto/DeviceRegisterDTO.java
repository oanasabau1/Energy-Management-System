package com.example.device_microservice.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DeviceRegisterDTO {
    Long userId;
    String description;
    String address;
    Double maxHourlyConsumption;
}
