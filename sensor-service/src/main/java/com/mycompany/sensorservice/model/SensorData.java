package com.mycompany.sensorservice.model;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Measurement(name = "sensor_data") // name of the influx measurement
public class SensorData {
    @Column(tag = true) // tags in influx
    private String sensorId;
    @Column
    private double temperature;
    @Column
    private double humidity;
    @Column
    private double energyUsage;
    @Column(timestamp = true)// marks this as the time column
    private Instant timestamp;
    
}
