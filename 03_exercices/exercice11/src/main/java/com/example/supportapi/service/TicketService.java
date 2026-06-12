package com.example.supportapi.service;

import com.example.supportapi.exception.TicketNotFoundException;
import com.example.supportapi.exception.TicketStatusConflictException;
import com.example.supportapi.model.Priority;
import com.example.supportapi.model.Ticket;
import com.example.supportapi.model.TicketStatus;
import com.example.supportapi.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Couche service : porte les règles métier des tickets.
 * Le repository est injecté par constructeur pour être facilement mocké en test.
 */
@Service
public class TicketService {

    private final TicketRepository repository;

    public TicketService(TicketRepository repository) {
        this.repository = repository;
    }

    /**
     * Crée un ticket : titre obligatoire (≥ 3 caractères utiles après trim),
     * priorité obligatoire, statut initial toujours OPEN.
     */
    public Ticket create(String title, Priority priority) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Le titre est obligatoire");
        }
        String cleanedTitle = title.trim();
        if (cleanedTitle.length() < 3) {
            throw new IllegalArgumentException("Le titre doit contenir au moins 3 caracteres");
        }
        if (priority == null) {
            throw new IllegalArgumentException("La priorite est obligatoire");
        }
        return repository.save(new Ticket(null, cleanedTitle, priority, TicketStatus.OPEN));
    }

    public Ticket getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException(id));
    }

    public List<Ticket> findAll() {
        return repository.findAll();
    }

    /**
     * Change le statut d'un ticket existant si la transition est autorisée.
     * Transitions valides : OPEN→IN_PROGRESS, OPEN→RESOLVED, IN_PROGRESS→RESOLVED.
     * Un ticket RESOLVED ne peut plus changer de statut.
     */
    public Ticket updateStatus(Long id, TicketStatus newStatus) {
        Ticket ticket = getById(id);
        if (!isTransitionAllowed(ticket.status(), newStatus)) {
            throw new TicketStatusConflictException(
                    "Transition de statut interdite : " + ticket.status() + " -> " + newStatus);
        }
        return repository.save(new Ticket(ticket.id(), ticket.title(), ticket.priority(), newStatus));
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    private boolean isTransitionAllowed(TicketStatus current, TicketStatus target) {
        return switch (current) {
            case OPEN -> target == TicketStatus.IN_PROGRESS || target == TicketStatus.RESOLVED;
            case IN_PROGRESS -> target == TicketStatus.RESOLVED;
            case RESOLVED -> false;
        };
    }
}
