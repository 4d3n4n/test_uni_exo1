package com.example.reservationapi.controller;

import com.example.reservationapi.dto.CreateRoomRequest;
import com.example.reservationapi.dto.RoomResponse;
import com.example.reservationapi.model.Room;
import com.example.reservationapi.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

/**
 * Endpoints REST des salles : mapping HTTP, validation DTO, conversion modèle ↔ DTO.
 */
@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService service;

    public RoomController(RoomService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<RoomResponse> create(@Valid @RequestBody CreateRoomRequest request) {
        Room created = service.create(request.name(), request.capacity());
        RoomResponse response = RoomResponse.from(created);
        return ResponseEntity
                .created(URI.create("/api/rooms/" + response.id()))
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<RoomResponse>> findAll() {
        List<RoomResponse> responses = service.findAll()
                .stream()
                .map(RoomResponse::from)
                .toList();
        return ResponseEntity.ok(responses);
    }
}
