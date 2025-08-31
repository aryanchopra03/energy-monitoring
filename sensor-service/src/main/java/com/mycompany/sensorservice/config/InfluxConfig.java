package com.mycompany.sensorservice.config;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import org.springframework.context.annotation.Bean;

public class InfluxConfig {
    private static final String url = "http://localhost:8086";
    private static final String token = "my-influxdb-token";
    private static final String org = "my-org";
    private static final String bucket = "sensor-bucket";

    @Bean
    public InfluxDBClient influxDBClient() {
        return InfluxDBClientFactory.create(url, token.toCharArray(), org, bucket);
    }
}
