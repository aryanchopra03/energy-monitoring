package com.mycompany.sensorservice.repository;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.QueryApi;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import com.mycompany.sensorservice.model.SensorData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Repository
public class SensorRepositoryImpl implements SensorRepository {
    private final InfluxDBClient influxDBClient;
    private final String bucket;
    private final String org;

    public SensorRepositoryImpl(
            @Value("${influx.url}") String url,
            //@Value("${influx.username}") String username,
            //@Value("${influx.password}") String password,
            @Value("${influx.token}") String token,
            @Value("${influx.bucket}") String bucket,
            @Value("${influx.org:my-org}") String org) {
        this.influxDBClient = InfluxDBClientFactory.create(url, token.toCharArray(), org, bucket);
        this.bucket = bucket;
        this.org = org;
    }

    @Override
    public void save(SensorData sensorData) {
        WriteApiBlocking writeApi = influxDBClient.getWriteApiBlocking();
        writeApi.writeMeasurement(bucket, org, WritePrecision.NS, sensorData);
    }

    @Override
    public List<SensorData> findBySensorId(String sensorId) {
        QueryApi queryApi = influxDBClient.getQueryApi();

        String flux = String.format(
                "from(bucket: \"%s\") |> range(start: -1h) |> filter(fn: (r) => r[\"sensorId\"] == \"%s\")",
                bucket, sensorId);
        return queryApi.query(flux, SensorData.class); // auto mapped directly
    }

    @Override
    public List<SensorData> findAll() {
        QueryApi queryApi = influxDBClient.getQueryApi();
        String flux = String.format("from(bucket: \"%s\") |> range(start: -1h)", bucket);
        return queryApi.query(flux, SensorData.class); // auto mapped directly
    }

    @Override
    public List<SensorData> findByTimeRange(Instant start, Instant end) {
        QueryApi queryApi = influxDBClient.getQueryApi();
        String flux = String.format(
                "from(bucket: \"%s\") " +
                        "|> range(start: %s, stop:  %s)",
                bucket,
                start.toString(),
                end.toString()
        );
        return queryApi.query(flux, SensorData.class); // auto mapped directly
    }
}

    /*private List<SensorData> mapToSensorData(List<FluxTable> tables) {
        List<SensorData> result = new ArrayList<>();

        for (FluxTable table : tables) {
            for (FluxRecord record : table.getRecords()) {

                SensorData data = new SensorData();

                data.setSensorId((String) record.getValueByKey("sensorId"));

                data.setTemperature(record.getValueByKey("temperature") != null ?
                        ((Number) record.getValueByKey("temperature")).doubleValue() : 0.0);

                data.setHumidity(record.getValueByKey("humidity") != null ?
                        ((Number) record.getValueByKey("humidity")).doubleValue() : 0.0);

                data.setEnergyUsage(record.getValueByKey("energyUsage") != null ?
                        ((Number) record.getValueByKey("energyUsage")).doubleValue() : 0.0);

                data.setTimestamp((Instant) record.getValueByKey("_time"));
                result.add(data);
            }
        }
        return result;
    }

     */



