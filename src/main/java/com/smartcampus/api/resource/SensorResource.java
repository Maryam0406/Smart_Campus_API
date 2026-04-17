package com.smartcampus.api.resource;

import com.smartcampus.api.model.Sensor;
import com.smartcampus.api.service.SensorService;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/api/v1/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    private SensorService sensorService = new SensorService();

    @GET
    public List<Sensor> getSensors() {
        return sensorService.getAllSensors();
    }

    @POST
    public Sensor addSensor(Sensor sensor) {
        return sensorService.addSensor(sensor);
    }
}