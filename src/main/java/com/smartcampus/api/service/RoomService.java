package com.smartcampus.api.service;

import com.smartcampus.api.exception.RoomNotFoundException;
import com.smartcampus.api.model.Room;
import java.util.*;

public class RoomService {

    private static Map<String, Room> rooms = new HashMap<>();

    // GET ALL ROOMS
    public List<Room> getAllRooms() {
        return new ArrayList<>(rooms.values());
    }

    // ADD ROOM
    public Room addRoom(Room room) {
        if (rooms.containsKey(room.getId())) {
            throw new RuntimeException("Room already exists");
        }
        rooms.put(room.getId(), room);
        return room;
    }

    // GET ROOM BY ID
    public Room getRoomById(String id) {
        Room room = rooms.get(id);
        if (room == null) {
            throw new RoomNotFoundException("Room not found");
        }
        return room;
    }

    // DELETE ROOM
    public void deleteRoom(String id) {
        if (!rooms.containsKey(id)) {
            throw new RoomNotFoundException("Room not found");
        }
        rooms.remove(id);
    }

    // LINK SENSOR TO ROOM
    public Room addSensorToRoom(String roomId, String sensorId) {

        Room room = rooms.get(roomId);

        if (room == null) {
            throw new RoomNotFoundException("Room not found");
        }

        // prevent duplicates
        if (!room.getSensorIds().contains(sensorId)) {
            room.getSensorIds().add(sensorId);
        }

        return room;
    }
}