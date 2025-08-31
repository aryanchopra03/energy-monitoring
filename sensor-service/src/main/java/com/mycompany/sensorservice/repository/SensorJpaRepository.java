package com.mycompany.sensorservice.repository;

import com.mycompany.sensorservice.entity.SensorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface SensorJpaRepository extends JpaRepository<SensorEntity, Long> {
    List<SensorEntity> findBySensorId(String SensorId);
    List<SensorEntity> findByTimestampBetween(Instant start, Instant end);
}
