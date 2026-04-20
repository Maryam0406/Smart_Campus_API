package com.smartcampus.api.resource;

import com.smartcampus.api.model.Room;
import com.smartcampus.api.service.RoomService;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/api/v1/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {

    private RoomService roomService = new RoomService();

    // GET ALL ROOMS
    @GET
    public List<Room> getRooms() {
        return roomService.getAllRooms();
    }

    // CREATE ROOM
    @POST
    public Room addRoom(Room room) {
        return roomService.addRoom(room);
    }

    // GET ROOM BY ID
    @GET
    @Path("/{id}")
    public Room getRoom(@PathParam("id") String id) {
        return roomService.getRoomById(id);
    }

    // DELETE ROOM 
    @DELETE
    @Path("/{id}")
    public Response deleteRoom(@PathParam("id") String id) {
        roomService.deleteRoom(id);
        return Response.noContent().build();
    }

    // LINK SENSOR TO ROOM
    @POST
    @Path("/{roomId}/sensors/{sensorId}")
    public Room addSensorToRoom(@PathParam("roomId") String roomId,
                               @PathParam("sensorId") String sensorId) {
        return roomService.addSensorToRoom(roomId, sensorId);
    }
}