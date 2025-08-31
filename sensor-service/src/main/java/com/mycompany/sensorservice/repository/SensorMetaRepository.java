package com.mycompany.sensorservice.repository;

import com.mycompany.sensorservice.model.SensorMeta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorMetaRepository extends JpaRepository<SensorMeta, Long> {
}
