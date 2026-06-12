package com.example.supportapi.controller;

import com.example.supportapi.dto.CreateTicketRequest;
import com.example.supportapi.dto.TicketResponse;
import com.example.supportapi.dto.UpdateTicketStatusRequest;
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

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService service;

    public TicketController(TicketService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TicketResponse> create(@Valid @RequestBody CreateTicketRequest request) {
        throw new UnsupportedOperationException("Controller non implemente en phase RED");
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketResponse> getById(@PathVariable Long id) {
        throw new UnsupportedOperationException("Controller non implemente en phase RED");
    }

    @GetMapping
    public ResponseEntity<List<TicketResponse>> findAll() {
        throw new UnsupportedOperationException("Controller non implemente en phase RED");
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TicketResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTicketStatusRequest request
    ) {
        throw new UnsupportedOperationException("Controller non implemente en phase RED");
    }
}
