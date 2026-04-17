package com.smartcampus.api.service;

import com.smartcampus.api.model.Sensor;
import java.util.*;

public class SensorService {

    private static Map<String, Sensor> sensors = new HashMap<>();

    public List<Sensor> getAllSensors() {
        return new ArrayList<>(sensors.values());
    }

    public Sensor addSensor(Sensor sensor) {
        sensors.put(sensor.getId(), sensor);
        return sensor;
    }
}