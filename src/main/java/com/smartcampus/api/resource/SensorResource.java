package com.smartcampus.api.resource;

import com.smartcampus.api.model.Sensor;
import com.smartcampus.api.service.SensorService;
import com.smartcampus.api.resource.SensorReadingResource; 

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/api/v1/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    private SensorService sensorService = new SensorService();

    @GET
    public List<Sensor> getSensors(@QueryParam("type") String type) {

        if (type == null) {
            return sensorService.getAllSensors();
        }

        return sensorService.getSensorsByType(type);
    }

    @POST
    public Sensor addSensor(Sensor sensor) {
        return sensorService.addSensor(sensor);
    }

  
    @Path("/{sensorId}/readings")
    public SensorReadingResource getReadings(@PathParam("sensorId") String sensorId) {
        return new SensorReadingResource(sensorId);
    }
}