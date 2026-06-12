package com.example.reservationapi.repository;

import com.example.reservationapi.model.Room;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class InMemoryRoomRepository implements RoomRepository {

    @Override
    public Room save(Room room) {
        throw new UnsupportedOperationException("Repository non implemente en phase RED");
    }

    @Override
    public Optional<Room> findById(Long id) {
        throw new UnsupportedOperationException("Repository non implemente en phase RED");
    }

    @Override
    public List<Room> findAll() {
        throw new UnsupportedOperationException("Repository non implemente en phase RED");
    }

    @Override
    public void deleteAll() {
        // No-op en phase RED : permet aux tests d'integration de demarrer leur parcours.
    }
}
