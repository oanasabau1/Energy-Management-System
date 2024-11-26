package com.example.monitoring_microservice.mapper;

import com.example.monitoring_microservice.dto.MeasurementDTO;
import com.example.monitoring_microservice.entity.Measurement;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class MeasurementMapper {

     public Measurement toMeasurement(String message) {
        Measurement measurement = new Measurement();
        String trimmedJson = message.replaceAll("[{}\"]", "");
        String[] keyValuePairs = trimmedJson.split(",");
        for (String pair : keyValuePairs) {
            String[] entry = pair.split(":", 2);
            if (entry.length == 2) {
                String key = entry[0].trim();
                String value = entry[1].trim();

                switch (key) {
                    case "timestamp" ->
                            measurement.setTimestamp(LocalDateTime.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SS")));
                    case "device_id" ->measurement.setDeviceId(Long.valueOf(value));
                    case "measurement_value" -> measurement.setValue(Double.parseDouble(value));
                }
            }
        }
        return measurement;
    }

    public MeasurementDTO toDTO(Measurement measurement) {
        MeasurementDTO measurementDTO = new MeasurementDTO();
        measurementDTO.setMeasurementId(measurement.getMeasurementId());
        measurementDTO.setDeviceId(measurement.getDeviceId());
        measurementDTO.setTimestamp(measurement.getTimestamp());
        measurementDTO.setValue(measurement.getValue());
        return measurementDTO;
    }
}
