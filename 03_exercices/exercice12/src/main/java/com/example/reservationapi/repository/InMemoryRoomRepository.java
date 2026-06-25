package com.example.reservationapi.repository;

import com.example.reservationapi.model.Room;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Stockage en mémoire des salles, avec génération d'identifiant à la création.
 */
@Repository
public class InMemoryRoomRepository implements RoomRepository {

    private final AtomicLong sequence = new AtomicLong(0);
    private final Map<Long, Room> rooms = new ConcurrentHashMap<>();

    @Override
    public Room save(Room room) {
        Long id = room.id() != null ? room.id() : sequence.incrementAndGet();
        Room stored = new Room(id, room.name(), room.capacity());
        rooms.put(id, stored);
        return stored;
    }

    @Override
    public Optional<Room> findById(Long id) {
        return Optional.ofNullable(rooms.get(id));
    }

    @Override
    public List<Room> findAll() {
        return new ArrayList<>(rooms.values())
                .stream()
                .sorted(Comparator.comparing(Room::id))
                .toList();
    }

    @Override
    public void deleteAll() {
        rooms.clear();
        sequence.set(0);
    }
}
