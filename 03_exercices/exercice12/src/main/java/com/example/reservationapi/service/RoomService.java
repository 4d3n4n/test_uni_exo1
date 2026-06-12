package com.example.reservationapi.service;

import com.example.reservationapi.model.Room;
import com.example.reservationapi.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    private final RoomRepository repository;

    public RoomService(RoomRepository repository) {
        this.repository = repository;
    }

    public Room create(String name, int capacity) {
        throw new UnsupportedOperationException("Service non implemente en phase RED");
    }

    public List<Room> findAll() {
        throw new UnsupportedOperationException("Service non implemente en phase RED");
    }
}
