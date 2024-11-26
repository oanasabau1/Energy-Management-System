package com.example.monitoring_microservice.repository;

import com.example.monitoring_microservice.entity.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, Long> {
    ArrayList<Measurement> findTop6ByDeviceIdOrderByTimestampDesc(Long deviceId); // to get the last measurements for a device that were recorded in the last hour

    @Query("select m from Measurement m where m.deviceId = ?1 and m.timestamp between ?2 and ?3 order by m.timestamp asc")
    List<Measurement> findByDeviceIdAndTimestampBetween(Long deviceId, LocalDateTime timestampStart, LocalDateTime timestampEnd);
}
