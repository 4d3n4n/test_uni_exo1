package com.example.supportapi.service;

import com.example.supportapi.exception.TicketNotFoundException;
import com.example.supportapi.exception.TicketStatusConflictException;
import com.example.supportapi.model.Priority;
import com.example.supportapi.model.Ticket;
import com.example.supportapi.model.TicketStatus;
import com.example.supportapi.repository.TicketRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository repository;

    @InjectMocks
    private TicketService service;

    @Test
    void shouldCreateTicket_whenTitleAndPriorityAreValid() {
        // Arrange
        when(repository.save(any(Ticket.class)))
                .thenReturn(new Ticket(1L, "Panne serveur", Priority.HIGH, TicketStatus.OPEN));

        // Act
        Ticket result = service.create("  Panne serveur  ", Priority.HIGH);

        // Assert
        assertEquals(1L, result.id());
        assertEquals("Panne serveur", result.title());
        assertEquals(Priority.HIGH, result.priority());
        assertEquals(TicketStatus.OPEN, result.status());
        verify(repository).save(argThat(ticket ->
                ticket.id() == null
                        && "Panne serveur".equals(ticket.title())
                        && ticket.priority() == Priority.HIGH
                        && ticket.status() == TicketStatus.OPEN
        ));
    }

    @Test
    void shouldCreateTicketWithOpenStatusByDefault() {
        // Arrange
        when(repository.save(any(Ticket.class)))
                .thenReturn(new Ticket(1L, "Demande support", Priority.MEDIUM, TicketStatus.OPEN));

        // Act
        Ticket result = service.create("Demande support", Priority.MEDIUM);

        // Assert
        assertEquals(TicketStatus.OPEN, result.status());
    }

    @Test
    void shouldRejectTicketCreation_whenTitleIsBlank() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> service.create("   ", Priority.LOW));
        verify(repository, never()).save(any(Ticket.class));
    }

    @Test
    void shouldRejectTicketCreation_whenTitleIsTooShortAfterTrim() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> service.create(" ab ", Priority.LOW));
        verify(repository, never()).save(any(Ticket.class));
    }

    @Test
    void shouldRejectTicketCreation_whenPriorityIsMissing() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> service.create("Panne serveur", null));
        verify(repository, never()).save(any(Ticket.class));
    }

    @Test
    void shouldReturnTicket_whenTicketExists() {
        // Arrange
        when(repository.findById(1L))
                .thenReturn(Optional.of(new Ticket(1L, "Panne serveur", Priority.HIGH, TicketStatus.OPEN)));

        // Act
        Ticket result = service.getById(1L);

        // Assert
        assertEquals(1L, result.id());
        assertEquals("Panne serveur", result.title());
        verify(repository).findById(1L);
    }

    @Test
    void shouldThrowNotFoundException_whenTicketDoesNotExist() {
        // Arrange
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(TicketNotFoundException.class, () -> service.getById(99L));
        verify(repository).findById(99L);
    }

    @Test
    void shouldUpdateStatus_whenTransitionFromOpenToInProgressIsAllowed() {
        // Arrange
        var existingTicket = new Ticket(1L, "Panne serveur", Priority.HIGH, TicketStatus.OPEN);
        var updatedTicket = new Ticket(1L, "Panne serveur", Priority.HIGH, TicketStatus.IN_PROGRESS);

        when(repository.findById(1L)).thenReturn(Optional.of(existingTicket));
        when(repository.save(updatedTicket)).thenReturn(updatedTicket);

        // Act
        Ticket result = service.updateStatus(1L, TicketStatus.IN_PROGRESS);

        // Assert
        assertEquals(TicketStatus.IN_PROGRESS, result.status());
        verify(repository).save(updatedTicket);
    }

    @Test
    void shouldUpdateStatus_whenTransitionFromOpenToResolvedIsAllowed() {
        // Arrange
        var existingTicket = new Ticket(1L, "Panne serveur", Priority.HIGH, TicketStatus.OPEN);
        var updatedTicket = new Ticket(1L, "Panne serveur", Priority.HIGH, TicketStatus.RESOLVED);

        when(repository.findById(1L)).thenReturn(Optional.of(existingTicket));
        when(repository.save(updatedTicket)).thenReturn(updatedTicket);

        // Act
        Ticket result = service.updateStatus(1L, TicketStatus.RESOLVED);

        // Assert
        assertEquals(TicketStatus.RESOLVED, result.status());
        verify(repository).save(updatedTicket);
    }

    @Test
    void shouldUpdateStatus_whenTransitionFromInProgressToResolvedIsAllowed() {
        // Arrange
        var existingTicket = new Ticket(1L, "Panne serveur", Priority.HIGH, TicketStatus.IN_PROGRESS);
        var updatedTicket = new Ticket(1L, "Panne serveur", Priority.HIGH, TicketStatus.RESOLVED);

        when(repository.findById(1L)).thenReturn(Optional.of(existingTicket));
        when(repository.save(updatedTicket)).thenReturn(updatedTicket);

        // Act
        Ticket result = service.updateStatus(1L, TicketStatus.RESOLVED);

        // Assert
        assertEquals(TicketStatus.RESOLVED, result.status());
        verify(repository).save(updatedTicket);
    }

    @Test
    void shouldRejectStatusUpdate_whenTicketIsAlreadyResolved() {
        // Arrange
        var existingTicket = new Ticket(1L, "Panne serveur", Priority.HIGH, TicketStatus.RESOLVED);
        when(repository.findById(1L)).thenReturn(Optional.of(existingTicket));

        // Act + Assert
        assertThrows(TicketStatusConflictException.class, () -> service.updateStatus(1L, TicketStatus.IN_PROGRESS));
        verify(repository, never()).save(any(Ticket.class));
    }
}
