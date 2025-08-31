package com.mycompany.sensorservice.service;

import com.mycompany.sensorservice.entity.SensorEntity;
import com.mycompany.sensorservice.model.SensorData;
import com.mycompany.sensorservice.repository.SensorJpaRepository;
import com.mycompany.sensorservice.repository.SensorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Random;

@Service
public class SensorService {

    private static final Logger logger = LoggerFactory.getLogger(SensorService.class);
    private final SensorJpaRepository sensorJpaRepository; // postgresql
    private final SensorRepository sensorRepository; // influxdb
    private final Random random = new Random();


    // constructor injection
    public SensorService(SensorRepository sensorRepository, SensorJpaRepository sensorJpaRepository) {
        this.sensorRepository = sensorRepository;
        this.sensorJpaRepository = sensorJpaRepository;
    }
    // generates and logs sensor data every 5 seconds

    @Scheduled(fixedRate = 5000)
    private void generateSensorData() {
        SensorData data = new SensorData(
                "sensor-" + random.nextInt(1000),
                20 + random.nextDouble() * 10, // temperature 20-30^C
                30 + random.nextDouble() * 40,            // humidity 30-70%
                100 + random.nextDouble() * 900,          // energy usage 100-1000 Kwh
                Instant.now()
        );

        // Save to influxdb
        sensorRepository.save(data);
        // for now, just log it
        logger.info("Generated Sensor Data: {}", data);
        //System.out.println("Generated Sensor Data: " + data);

    }
    // Hybrid save method
    public void saveSensorData(SensorData data) {
        // save to influxdb
        sensorRepository.save(data);

        // save to postgresql
        SensorEntity entity = SensorEntity.builder()
                .sensorId(data.getSensorId())
                .temperature(data.getTemperature())
                .humidity(data.getHumidity())
                .energyUsage(data.getEnergyUsage())
                .timestamp(data.getTimestamp())
                .build();

        sensorJpaRepository.save(entity);

    }
    // get all sensor data
    public List<SensorData> getAllSensorData() {
        return sensorRepository.findAll();
    }

    // get sensor data by id
    public List<SensorData> getSensorDataById(String sensorId) {
        return sensorRepository.findBySensorId(sensorId);
    }
    // get sensor data by time range
    public List<SensorData> getSensorDataByTimeRange(Instant start, Instant end) {
        return sensorRepository.findByTimeRange(start, end);
    }

}
