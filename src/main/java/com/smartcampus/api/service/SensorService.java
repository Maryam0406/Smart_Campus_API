package com.smartcampus.api.service;

import com.smartcampus.api.model.Sensor;
import com.smartcampus.api.exception.DuplicateResourceException;
import com.smartcampus.api.exception.SensorNotFoundException; 
import java.util.*;

public class SensorService {

    private static Map<String, Sensor> sensors = new HashMap<>();

    public List<Sensor> getAllSensors() {
        return new ArrayList<>(sensors.values());
    }
    public Sensor getSensorById(String id) {
        Sensor sensor = sensors.get(id);
        if (sensor == null) {
            throw new SensorNotFoundException("Sensor not found");
        }
        return sensor;
    }

    public Sensor addSensor(Sensor sensor) {
        if (sensors.containsKey(sensor.getId())) {
            throw new DuplicateResourceException("Sensor already exists");
        }

        try {
            new RoomService().getRoomById(sensor.getRoomId());
        } catch (com.smartcampus.api.exception.RoomNotFoundException e) {
            throw new com.smartcampus.api.exception.LinkedResourceNotFoundException("The specified roomId does not exist.");
        }

        sensors.put(sensor.getId(), sensor);
        return sensor;
    }
    public static boolean sensorExists(String id) {
        return sensors.containsKey(id);
    }

    public List<Sensor> getSensorsByType(String type) {
        List<Sensor> result = new ArrayList<>();

        for (Sensor s : sensors.values()) {
            if (s.getType().equalsIgnoreCase(type)) {
                result.add(s);
            }
        }

        return result;
    }

    public void updateSensorValue(String sensorId, double value) {

        Sensor sensor = sensors.get(sensorId);

        if (sensor == null) {
            throw new SensorNotFoundException("Sensor not found");
        }

        sensor.setValue(value);
    }
}