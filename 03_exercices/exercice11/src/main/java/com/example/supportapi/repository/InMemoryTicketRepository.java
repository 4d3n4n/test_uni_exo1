package com.example.supportapi.repository;

import com.example.supportapi.model.Ticket;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Implémentation en mémoire du repository de tickets.
 * Génère un identifiant à la création et conserve les tickets dans une map.
 */
@Repository
public class InMemoryTicketRepository implements TicketRepository {

    private final AtomicLong sequence = new AtomicLong(0);
    private final Map<Long, Ticket> tickets = new ConcurrentHashMap<>();

    /**
     * Sauvegarde un ticket : génère un identifiant si absent (création),
     * sinon écrase le ticket existant (mise à jour de statut).
     */
    @Override
    public Ticket save(Ticket ticket) {
        Long id = ticket.id() != null ? ticket.id() : sequence.incrementAndGet();
        Ticket stored = new Ticket(id, ticket.title(), ticket.priority(), ticket.status());
        tickets.put(id, stored);
        return stored;
    }

    @Override
    public Optional<Ticket> findById(Long id) {
        return Optional.ofNullable(tickets.get(id));
    }

    @Override
    public List<Ticket> findAll() {
        return new ArrayList<>(tickets.values())
                .stream()
                .sorted(Comparator.comparing(Ticket::id))
                .toList();
    }

    @Override
    public void deleteAll() {
        tickets.clear();
        sequence.set(0);
    }
}
