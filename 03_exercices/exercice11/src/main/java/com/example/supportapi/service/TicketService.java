package com.example.supportapi.service;

import com.example.supportapi.model.Priority;
import com.example.supportapi.model.Ticket;
import com.example.supportapi.model.TicketStatus;
import com.example.supportapi.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {

    private final TicketRepository repository;

    public TicketService(TicketRepository repository) {
        this.repository = repository;
    }

    public Ticket create(String title, Priority priority) {
        throw new UnsupportedOperationException("Service non implemente en phase RED");
    }

    public Ticket getById(Long id) {
        throw new UnsupportedOperationException("Service non implemente en phase RED");
    }

    public List<Ticket> findAll() {
        throw new UnsupportedOperationException("Service non implemente en phase RED");
    }

    public Ticket updateStatus(Long id, TicketStatus newStatus) {
        throw new UnsupportedOperationException("Service non implemente en phase RED");
    }

    public void deleteAll() {
        throw new UnsupportedOperationException("Service non implemente en phase RED");
    }
}
