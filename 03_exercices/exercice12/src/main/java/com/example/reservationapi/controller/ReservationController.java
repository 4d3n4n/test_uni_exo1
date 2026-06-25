package com.example.reservationapi.controller;

import com.example.reservationapi.dto.CreateReservationRequest;
import com.example.reservationapi.dto.ReservationResponse;
import com.example.reservationapi.model.Reservation;
import com.example.reservationapi.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

/**
 * Endpoints REST des réservations : création, consultation, annulation.
 */
@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService service;

    public ReservationController(ReservationService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> create(@Valid @RequestBody CreateReservationRequest request) {
        Reservation created = service.create(
                request.roomId(), request.requester(), request.start(), request.end());
        ReservationResponse response = ReservationResponse.from(created);
        return ResponseEntity
                .created(URI.create("/api/reservations/" + response.id()))
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ReservationResponse.from(service.getById(id)));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ReservationResponse> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(ReservationResponse.from(service.cancel(id)));
    }
}
