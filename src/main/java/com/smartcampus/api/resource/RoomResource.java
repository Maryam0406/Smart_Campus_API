package com.smartcampus.api.resource;

import com.smartcampus.api.model.Room;
import com.smartcampus.api.service.RoomService;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/api/v1/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {

    private RoomService roomService = new RoomService();

    @GET
    public List<Room> getRooms() {
        return roomService.getAllRooms();
    }

    @POST
    public Room addRoom(Room room) {
        return roomService.addRoom(room);
    }
}