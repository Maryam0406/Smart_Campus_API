package com.smartcampus.api.resource;

import com.smartcampus.api.model.SensorReading;
import com.smartcampus.api.service.SensorService;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.*;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorReadingResource {

    private static Map<String, List<SensorReading>> readings = new HashMap<>();

    private String sensorId;

    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    // Get readings
    @GET
    public List<SensorReading> getReadings() {
        return readings.getOrDefault(sensorId, new ArrayList<>());
    }

    // Add reading
    @POST
    public SensorReading addReading(SensorReading reading) {

        readings.computeIfAbsent(sensorId, k -> new ArrayList<>()).add(reading);

        // update sensor value
        new SensorService().updateSensorValue(sensorId, reading.getValue());

        return reading;
    }
}