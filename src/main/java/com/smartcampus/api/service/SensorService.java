package com.smartcampus.api.service;

import com.smartcampus.api.model.Sensor;
import java.util.*;

public class SensorService {

    private static Map<String, Sensor> sensors = new HashMap<>();

    // GET ALL SENSORS
    public List<Sensor> getAllSensors() {
        return new ArrayList<>(sensors.values());
    }

    // ADD SENSOR
    public Sensor addSensor(Sensor sensor) {
        sensors.put(sensor.getId(), sensor);
        return sensor;
    }
    public static boolean sensorExists(String id) {
        return sensors.containsKey(id);
    }
}