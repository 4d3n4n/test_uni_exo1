package com.example.reservationapi.controller;

import com.example.reservationapi.model.Room;
import com.example.reservationapi.service.RoomService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RoomController.class)
@Import(ReservationApiExceptionHandler.class)
class RoomControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RoomService service;

    @Test
    void shouldReturnCreated_whenCreateRoomRequestIsValid() throws Exception {
        // Arrange
        when(service.create("Salle A", 10)).thenReturn(new Room(1L, "Salle A", 10));

        // Act + Assert
        mockMvc.perform(post("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Salle A\",\"capacity\":10}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/rooms/1"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Salle A"))
                .andExpect(jsonPath("$.capacity").value(10));

        verify(service).create("Salle A", 10);
    }

    @Test
    void shouldReturnBadRequest_whenCreateRoomRequestIsInvalid() throws Exception {
        // Act + Assert : nom vide -> 400
        mockMvc.perform(post("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\",\"capacity\":10}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Le nom de la salle est obligatoire"));

        verify(service, never()).create(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.anyInt());
    }

    @Test
    void shouldReturnOk_whenListingRooms() throws Exception {
        // Arrange
        when(service.findAll()).thenReturn(List.of(
                new Room(1L, "Salle A", 10),
                new Room(2L, "Salle B", 4)));

        // Act + Assert
        mockMvc.perform(get("/api/rooms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Salle A"))
                .andExpect(jsonPath("$[1].capacity").value(4));

        verify(service).findAll();
    }
}
