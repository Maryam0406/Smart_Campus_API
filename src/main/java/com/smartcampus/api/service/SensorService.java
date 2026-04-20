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

    // CHECK IF SENSOR EXISTS
    public static boolean sensorExists(String id) {
        return sensors.containsKey(id);
    }

    // 🔥 NEW METHOD (FILTERING)
    public List<Sensor> getSensorsByType(String type) {
        List<Sensor> result = new ArrayList<>();

        for (Sensor s : sensors.values()) {
            if (s.getType().equalsIgnoreCase(type)) {
                result.add(s);
            }
        }

        return result;
    }
}