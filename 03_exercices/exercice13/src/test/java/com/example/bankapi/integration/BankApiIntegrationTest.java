package com.example.bankapi.integration;

import com.example.bankapi.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BankApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    void shouldCreateDepositTransferAndConsult_withRealSpringContext() throws Exception {
        // 1. Créer deux comptes
        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"number\":\"C1\",\"holder\":\"Alice\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.balance").value(0));

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"number\":\"C2\",\"holder\":\"Bob\"}"))
                .andExpect(status().isCreated());

        // 2. Déposer sur C1
        mockMvc.perform(post("/accounts/C1/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":100}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(100));

        // 3. Virer de C1 vers C2
        mockMvc.perform(post("/accounts/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"from\":\"C1\",\"to\":\"C2\",\"amount\":60}"))
                .andExpect(status().isOk());

        // 4. Consulter les soldes résultants
        mockMvc.perform(get("/accounts/C1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(40));

        mockMvc.perform(get("/accounts/C2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(60));
    }
}
