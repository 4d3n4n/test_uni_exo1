package com.example.supportapi.controller;

import com.example.supportapi.exception.TicketNotFoundException;
import com.example.supportapi.exception.TicketStatusConflictException;
import com.example.supportapi.model.Priority;
import com.example.supportapi.model.Ticket;
import com.example.supportapi.model.TicketStatus;
import com.example.supportapi.service.TicketService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TicketController.class)
@Import(TicketApiExceptionHandler.class)
class TicketControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TicketService service;

    @Test
    void shouldReturnCreated_whenCreateRequestIsValid() throws Exception {
        // Arrange
        when(service.create("Panne serveur", Priority.HIGH))
                .thenReturn(new Ticket(1L, "Panne serveur", Priority.HIGH, TicketStatus.OPEN));

        // Act + Assert
        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Panne serveur\",\"priority\":\"HIGH\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/tickets/1"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Panne serveur"))
                .andExpect(jsonPath("$.priority").value("HIGH"))
                .andExpect(jsonPath("$.status").value("OPEN"));

        verify(service).create("Panne serveur", Priority.HIGH);
    }

    @Test
    void shouldReturnBadRequest_whenCreateRequestIsInvalid() throws Exception {
        // Act + Assert
        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"ab\",\"priority\":\"HIGH\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Le titre doit contenir au moins 3 caracteres"));

        verify(service, never()).create(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any());
    }

    @Test
    void shouldReturnOk_whenTicketExists() throws Exception {
        // Arrange
        when(service.getById(1L))
                .thenReturn(new Ticket(1L, "Panne serveur", Priority.HIGH, TicketStatus.OPEN));

        // Act + Assert
        mockMvc.perform(get("/api/tickets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Panne serveur"))
                .andExpect(jsonPath("$.priority").value("HIGH"))
                .andExpect(jsonPath("$.status").value("OPEN"));

        verify(service).getById(1L);
    }

    @Test
    void shouldReturnOk_whenListingTickets() throws Exception {
        // Arrange
        when(service.findAll()).thenReturn(List.of(
                new Ticket(1L, "Panne serveur", Priority.HIGH, TicketStatus.OPEN),
                new Ticket(2L, "Question utilisateur", Priority.LOW, TicketStatus.IN_PROGRESS)
        ));

        // Act + Assert
        mockMvc.perform(get("/api/tickets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title").value("Panne serveur"))
                .andExpect(jsonPath("$[1].status").value("IN_PROGRESS"));

        verify(service).findAll();
    }

    @Test
    void shouldReturnNotFound_whenTicketDoesNotExist() throws Exception {
        // Arrange
        when(service.getById(99L)).thenThrow(new TicketNotFoundException(99L));

        // Act + Assert
        mockMvc.perform(get("/api/tickets/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Aucun ticket trouve avec l'identifiant 99"));

        verify(service).getById(99L);
    }

    @Test
    void shouldReturnOk_whenUpdatingStatus() throws Exception {
        // Arrange
        when(service.updateStatus(1L, TicketStatus.IN_PROGRESS))
                .thenReturn(new Ticket(1L, "Panne serveur", Priority.HIGH, TicketStatus.IN_PROGRESS));

        // Act + Assert
        mockMvc.perform(patch("/api/tickets/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"IN_PROGRESS\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));

        verify(service).updateStatus(1L, TicketStatus.IN_PROGRESS);
    }

    @Test
    void shouldReturnConflict_whenStatusTransitionIsRejected() throws Exception {
        // Arrange
        when(service.updateStatus(1L, TicketStatus.IN_PROGRESS))
                .thenThrow(new TicketStatusConflictException("Un ticket resolu ne peut plus changer de statut"));

        // Act + Assert
        mockMvc.perform(patch("/api/tickets/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"IN_PROGRESS\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value("Un ticket resolu ne peut plus changer de statut"));

        verify(service).updateStatus(1L, TicketStatus.IN_PROGRESS);
    }
}
