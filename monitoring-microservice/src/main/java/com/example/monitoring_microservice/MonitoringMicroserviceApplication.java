package com.example.monitoring_microservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class MonitoringMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MonitoringMicroserviceApplication.class, args);
	}

}
