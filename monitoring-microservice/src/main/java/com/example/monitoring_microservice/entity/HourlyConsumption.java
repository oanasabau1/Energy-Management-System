package com.example.monitoring_microservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "hourly_consumption")
public class HourlyConsumption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hourlyConsumptionId", nullable = false)
    private Long hourlyConsumptionId;

    @Column(name = "deviceId", nullable = false)
    private Long deviceId;

    @Column(name = "userId", nullable = false)
    private Long userId;

    @Column(name = "maxHourlyConsumption", nullable = false)
    private Double maxHourlyConsumption;

    @Column(name = "consumption")
    private Double consumption;
}
