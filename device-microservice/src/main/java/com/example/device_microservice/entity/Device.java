package com.example.device_microservice.entity;

import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deviceId", unique = true, nullable = false)
    private Long deviceId;

    @Column(name = "userId", nullable = false)
    private Long userId;

    @Column(name = "description")
    private String description;

    @Column(name = "address")
    private String address;

    @Column(name = "maxHourlyConsumption")
    private Double maxHourlyConsumption;
}
