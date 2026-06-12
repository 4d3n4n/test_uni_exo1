package com.example.reservationapi.service;

import com.example.reservationapi.model.Room;
import com.example.reservationapi.repository.RoomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

    @Mock
    private RoomRepository repository;

    @InjectMocks
    private RoomService service;

    @Test
    void shouldCreateRoom_whenNameAndCapacityAreValid() {
        // Arrange
        when(repository.save(any(Room.class))).thenReturn(new Room(1L, "Salle A", 10));

        // Act
        Room result = service.create("  Salle A  ", 10);

        // Assert
        assertEquals(1L, result.id());
        assertEquals("Salle A", result.name());
        assertEquals(10, result.capacity());
        verify(repository).save(argThat(room ->
                room.id() == null
                        && "Salle A".equals(room.name())
                        && room.capacity() == 10));
    }

    @Test
    void shouldRejectRoomCreation_whenNameIsBlank() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> service.create("   ", 10));
        verify(repository, never()).save(any(Room.class));
    }

    @Test
    void shouldRejectRoomCreation_whenCapacityIsLessThanOne() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> service.create("Salle A", 0));
        verify(repository, never()).save(any(Room.class));
    }

    @Test
    void shouldReturnAllRooms() {
        // Arrange
        when(repository.findAll()).thenReturn(List.of(
                new Room(1L, "Salle A", 10),
                new Room(2L, "Salle B", 4)));

        // Act
        List<Room> result = service.findAll();

        // Assert
        assertEquals(2, result.size());
        verify(repository).findAll();
    }
}
