package com.mycompany.sensorservice.controller;

import com.mycompany.sensorservice.model.SensorData;
import com.mycompany.sensorservice.service.SensorService;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping ("/api/sensor")
public class SensorController {

    private final Random random = new Random();
    private final SensorService sensorService;

    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @GetMapping("/simulate-once") // simulate one reading (without saving)
    public SensorData simulateOnce() {
        return generateRandomSensorData();

    }
    // simulate one reading and save to influxdb
    @PostMapping("/simulate-and-save")
    public String simulateSave() {
        SensorData data = generateRandomSensorData();
        sensorService.saveSensorData(data);
        return "Simulated and saved: " + data;
    }

    // fetch all sensor data (last 1h from repo)
    @GetMapping("/all")
    public List<SensorData> getAll() {
        return sensorService.getAllSensorData();
    }

    // fetch data by sensorId
    @GetMapping("/{sensorId}")
    public List<SensorData> getDataBySensorId(@PathVariable String sensorId) {
        return sensorService.getSensorDataById(sensorId);
    }

    // helper method to generate random data
    private SensorData generateRandomSensorData() {
        return new SensorData(
                "sensor-" + random.nextInt(1000), //
                20 + random.nextDouble() * 10, // temperature 20-30^C
                30 + random.nextDouble() * 40, // humidity 30-70%
                100 + random.nextDouble() * 900, // energy usage 100-1000 kwh
                Instant.now()
        );
    }
}
