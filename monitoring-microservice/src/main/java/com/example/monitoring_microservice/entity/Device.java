package com.example.monitoring_microservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "device")
public class Device {
    @Id
    @Column(name = "deviceId", nullable = false)
    private Long deviceId;

    @Column(name = "userId", nullable = false)
    private Long userId;

    @Column(name = "maxHourlyConsumption", nullable = false)
    private Double maxHourlyConsumption;
}
