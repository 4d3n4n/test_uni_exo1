package com.example.reservationapi.controller;

import com.example.reservationapi.exception.ReservationConflictException;
import com.example.reservationapi.exception.ReservationNotFoundException;
import com.example.reservationapi.exception.RoomNotFoundException;
import com.example.reservationapi.model.Reservation;
import com.example.reservationapi.model.ReservationStatus;
import com.example.reservationapi.service.ReservationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReservationController.class)
@Import(ReservationApiExceptionHandler.class)
class ReservationControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReservationService service;

    private static final LocalDateTime START = LocalDateTime.of(2026, 6, 10, 14, 0);
    private static final LocalDateTime END = LocalDateTime.of(2026, 6, 10, 15, 0);

    private static final String VALID_BODY =
            "{\"roomId\":1,\"requester\":\"Alice\",\"start\":\"2026-06-10T14:00:00\",\"end\":\"2026-06-10T15:00:00\"}";

    @Test
    void shouldReturnCreated_whenCreateReservationRequestIsValid() throws Exception {
        // Arrange
        when(service.create(eq(1L), eq("Alice"), eq(START), eq(END)))
                .thenReturn(new Reservation(5L, 1L, "Alice", START, END, ReservationStatus.CONFIRMED));

        // Act + Assert
        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_BODY))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/reservations/5"))
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.roomId").value(1))
                .andExpect(jsonPath("$.status").value("CONFIRMED"));

        verify(service).create(eq(1L), eq("Alice"), eq(START), eq(END));
    }

    @Test
    void shouldReturnNotFound_whenRoomDoesNotExist() throws Exception {
        // Arrange
        when(service.create(eq(999L), eq("Alice"), eq(START), eq(END)))
                .thenThrow(new RoomNotFoundException(999L));

        String body =
                "{\"roomId\":999,\"requester\":\"Alice\",\"start\":\"2026-06-10T14:00:00\",\"end\":\"2026-06-10T15:00:00\"}";

        // Act + Assert
        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Aucune salle trouvee avec l'identifiant 999"));
    }

    @Test
    void shouldReturnConflict_whenSlotOverlaps() throws Exception {
        // Arrange
        when(service.create(eq(1L), eq("Alice"), eq(START), eq(END)))
                .thenThrow(new ReservationConflictException("Le creneau chevauche une reservation existante"));

        // Act + Assert
        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_BODY))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value("Le creneau chevauche une reservation existante"));
    }

    @Test
    void shouldReturnOk_whenReservationExists() throws Exception {
        // Arrange
        when(service.getById(5L))
                .thenReturn(new Reservation(5L, 1L, "Alice", START, END, ReservationStatus.CONFIRMED));

        // Act + Assert
        mockMvc.perform(get("/api/reservations/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.status").value("CONFIRMED"));

        verify(service).getById(5L);
    }

    @Test
    void shouldReturnNotFound_whenReservationDoesNotExist() throws Exception {
        // Arrange
        when(service.getById(99L)).thenThrow(new ReservationNotFoundException(99L));

        // Act + Assert
        mockMvc.perform(get("/api/reservations/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Aucune reservation trouvee avec l'identifiant 99"));
    }

    @Test
    void shouldReturnOk_whenCancellingReservation() throws Exception {
        // Arrange
        when(service.cancel(5L))
                .thenReturn(new Reservation(5L, 1L, "Alice", START, END, ReservationStatus.CANCELLED));

        // Act + Assert
        mockMvc.perform(patch("/api/reservations/5/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.status").value("CANCELLED"));

        verify(service).cancel(5L);
    }

    @Test
    void shouldReturnConflict_whenCancellingAlreadyCancelledReservation() throws Exception {
        // Arrange
        when(service.cancel(5L))
                .thenThrow(new ReservationConflictException("La reservation est deja annulee"));

        // Act + Assert
        mockMvc.perform(patch("/api/reservations/5/cancel"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value("La reservation est deja annulee"));
    }
}
