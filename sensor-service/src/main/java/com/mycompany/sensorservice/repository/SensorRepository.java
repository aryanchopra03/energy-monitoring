package com.mycompany.sensorservice.repository;

import com.mycompany.sensorservice.model.SensorData;

import java.time.Instant;
import java.util.List;

public interface SensorRepository {

    void save(SensorData sensorData);
    List<SensorData> findBySensorId(String sensorID);
    List<SensorData> findAll();

    List<SensorData> findByTimeRange(Instant start, Instant end);
}
