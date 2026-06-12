package com.example.reservationapi.service;

import com.example.reservationapi.exception.ReservationConflictException;
import com.example.reservationapi.exception.ReservationNotFoundException;
import com.example.reservationapi.exception.RoomNotFoundException;
import com.example.reservationapi.model.Reservation;
import com.example.reservationapi.model.ReservationStatus;
import com.example.reservationapi.model.Room;
import com.example.reservationapi.repository.ReservationRepository;
import com.example.reservationapi.repository.RoomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationService service;

    private static final LocalDateTime START = LocalDateTime.of(2026, 6, 10, 14, 0);
    private static final LocalDateTime END = LocalDateTime.of(2026, 6, 10, 15, 0);

    @Test
    void shouldCreateReservation_whenRoomExistsAndSlotIsFree() {
        // Arrange
        when(roomRepository.findById(1L)).thenReturn(Optional.of(new Room(1L, "Salle A", 10)));
        when(reservationRepository.findByRoomId(1L)).thenReturn(List.of());
        when(reservationRepository.save(any(Reservation.class)))
                .thenReturn(new Reservation(5L, 1L, "Alice", START, END, ReservationStatus.CONFIRMED));

        // Act
        Reservation result = service.create(1L, "Alice", START, END);

        // Assert
        assertEquals(5L, result.id());
        assertEquals(ReservationStatus.CONFIRMED, result.status());
        verify(reservationRepository).save(any(Reservation.class));
    }

    @Test
    void shouldRejectReservation_whenRoomDoesNotExist() {
        // Arrange
        when(roomRepository.findById(99L)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(RoomNotFoundException.class, () -> service.create(99L, "Alice", START, END));
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void shouldRejectReservation_whenRequesterIsBlank() {
        // Arrange
        when(roomRepository.findById(1L)).thenReturn(Optional.of(new Room(1L, "Salle A", 10)));

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> service.create(1L, "   ", START, END));
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void shouldRejectReservation_whenSlotIsInvalid() {
        // Arrange
        when(roomRepository.findById(1L)).thenReturn(Optional.of(new Room(1L, "Salle A", 10)));

        // Act + Assert : fin avant ou egale au debut
        assertThrows(IllegalArgumentException.class, () -> service.create(1L, "Alice", END, START));
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void shouldRejectReservation_whenSlotOverlapsExistingReservation() {
        // Arrange : une reservation confirmee chevauche le creneau demande
        when(roomRepository.findById(1L)).thenReturn(Optional.of(new Room(1L, "Salle A", 10)));
        when(reservationRepository.findByRoomId(1L)).thenReturn(List.of(
                new Reservation(7L, 1L, "Bob",
                        LocalDateTime.of(2026, 6, 10, 14, 30),
                        LocalDateTime.of(2026, 6, 10, 15, 30),
                        ReservationStatus.CONFIRMED)));

        // Act + Assert
        assertThrows(ReservationConflictException.class, () -> service.create(1L, "Alice", START, END));
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void shouldReturnReservation_whenItExists() {
        // Arrange
        when(reservationRepository.findById(5L)).thenReturn(Optional.of(
                new Reservation(5L, 1L, "Alice", START, END, ReservationStatus.CONFIRMED)));

        // Act
        Reservation result = service.getById(5L);

        // Assert
        assertEquals(5L, result.id());
        verify(reservationRepository).findById(5L);
    }

    @Test
    void shouldThrowNotFound_whenReservationDoesNotExist() {
        // Arrange
        when(reservationRepository.findById(99L)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(ReservationNotFoundException.class, () -> service.getById(99L));
    }

    @Test
    void shouldCancelReservation_whenItIsConfirmed() {
        // Arrange
        when(reservationRepository.findById(5L)).thenReturn(Optional.of(
                new Reservation(5L, 1L, "Alice", START, END, ReservationStatus.CONFIRMED)));
        when(reservationRepository.save(any(Reservation.class)))
                .thenReturn(new Reservation(5L, 1L, "Alice", START, END, ReservationStatus.CANCELLED));

        // Act
        Reservation result = service.cancel(5L);

        // Assert
        assertEquals(ReservationStatus.CANCELLED, result.status());
        verify(reservationRepository).save(any(Reservation.class));
    }

    @Test
    void shouldRejectCancellation_whenReservationIsAlreadyCancelled() {
        // Arrange
        when(reservationRepository.findById(5L)).thenReturn(Optional.of(
                new Reservation(5L, 1L, "Alice", START, END, ReservationStatus.CANCELLED)));

        // Act + Assert
        assertThrows(ReservationConflictException.class, () -> service.cancel(5L));
        verify(reservationRepository, never()).save(any(Reservation.class));
    }
}
