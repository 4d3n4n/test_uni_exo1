package com.example.supportapi.repository;

import com.example.supportapi.model.Ticket;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class InMemoryTicketRepository implements TicketRepository {

    @Override
    public Ticket save(Ticket ticket) {
        throw new UnsupportedOperationException("Repository non implemente en phase RED");
    }

    @Override
    public Optional<Ticket> findById(Long id) {
        throw new UnsupportedOperationException("Repository non implemente en phase RED");
    }

    @Override
    public List<Ticket> findAll() {
        throw new UnsupportedOperationException("Repository non implemente en phase RED");
    }

    @Override
    public void deleteAll() {
        // No-op en phase RED : permet aux tests d'integration de demarrer leur parcours.
    }
}
