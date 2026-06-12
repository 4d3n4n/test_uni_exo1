package com.example.reservationapi.dto;

import com.example.reservationapi.model.Room;

public record RoomResponse(Long id, String name, int capacity) {

    public static RoomResponse from(Room room) {
        return new RoomResponse(room.id(), room.name(), room.capacity());
    }
}
