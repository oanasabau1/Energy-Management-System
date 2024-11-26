package com.example.monitoring_microservice.repository;

import com.example.monitoring_microservice.entity.HourlyConsumption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HourlyConsumptionRepository extends JpaRepository<HourlyConsumption, Long> {
}
