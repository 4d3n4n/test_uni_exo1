package com.example.reservationapi.service;

import com.example.reservationapi.model.Room;
import com.example.reservationapi.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Règles métier des salles : nom obligatoire, capacité ≥ 1.
 */
@Service
public class RoomService {

    private final RoomRepository repository;

    public RoomService(RoomRepository repository) {
        this.repository = repository;
    }

    public Room create(String name, int capacity) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Le nom de la salle est obligatoire");
        }
        if (capacity < 1) {
            throw new IllegalArgumentException("La capacite doit etre superieure ou egale a 1");
        }
        return repository.save(new Room(null, name.trim(), capacity));
    }

    public List<Room> findAll() {
        return repository.findAll();
    }
}
