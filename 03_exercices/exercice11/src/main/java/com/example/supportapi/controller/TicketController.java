package com.example.supportapi.controller;

import com.example.supportapi.dto.CreateTicketRequest;
import com.example.supportapi.dto.TicketResponse;
import com.example.supportapi.dto.UpdateTicketStatusRequest;
import com.example.supportapi.model.Ticket;
import com.example.supportapi.service.TicketService;
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
import java.util.List;

/**
 * Couche web REST des tickets : mapping HTTP, validation des DTO,
 * conversion modèle ↔ DTO. Aucune règle métier ici (déléguée au service).
 */
@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService service;

    public TicketController(TicketService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TicketResponse> create(@Valid @RequestBody CreateTicketRequest request) {
        Ticket created = service.create(request.title(), request.priority());
        TicketResponse response = TicketResponse.from(created);
        return ResponseEntity
                .created(URI.create("/api/tickets/" + response.id()))
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(TicketResponse.from(service.getById(id)));
    }

    @GetMapping
    public ResponseEntity<List<TicketResponse>> findAll() {
        List<TicketResponse> responses = service.findAll()
                .stream()
                .map(TicketResponse::from)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TicketResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTicketStatusRequest request
    ) {
        Ticket updated = service.updateStatus(id, request.status());
        return ResponseEntity.ok(TicketResponse.from(updated));
    }
}
