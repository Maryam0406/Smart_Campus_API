package com.smartcampus.api.service;

import com.smartcampus.api.model.Room;
import java.util.*;

public class RoomService {

    private static Map<String, Room> rooms = new HashMap<>();

    public List<Room> getAllRooms() {
        return new ArrayList<>(rooms.values());
    }

    public Room addRoom(Room room) {
        rooms.put(room.getId(), room);
        return room;
    }
}