package com.example.bankapi.controller;

import com.example.bankapi.exception.AccountAlreadyExistsException;
import com.example.bankapi.exception.AccountNotFoundException;
import com.example.bankapi.exception.InsufficientFundsException;
import com.example.bankapi.model.Account;
import com.example.bankapi.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
@Import(BankApiExceptionHandler.class)
class AccountControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AccountService service;

    @Test
    void shouldReturnCreated_whenCreateRequestIsValid() throws Exception {
        when(service.create("C1", "Alice")).thenReturn(new Account("C1", "Alice", BigDecimal.ZERO));

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"number\":\"C1\",\"holder\":\"Alice\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/accounts/C1"))
                .andExpect(jsonPath("$.number").value("C1"))
                .andExpect(jsonPath("$.holder").value("Alice"))
                .andExpect(jsonPath("$.balance").value(0));

        verify(service).create("C1", "Alice");
    }

    @Test
    void shouldReturnBadRequest_whenCreateRequestIsInvalid() throws Exception {
        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"number\":\"\",\"holder\":\"Alice\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Le numero de compte est obligatoire"));
    }

    @Test
    void shouldReturnConflict_whenAccountAlreadyExists() throws Exception {
        when(service.create("C1", "Alice")).thenThrow(new AccountAlreadyExistsException("C1"));

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"number\":\"C1\",\"holder\":\"Alice\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    void shouldReturnOk_whenAccountExists() throws Exception {
        when(service.getByNumber("C1")).thenReturn(new Account("C1", "Alice", BigDecimal.valueOf(100)));

        mockMvc.perform(get("/accounts/C1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").value("C1"))
                .andExpect(jsonPath("$.balance").value(100));

        verify(service).getByNumber("C1");
    }

    @Test
    void shouldReturnNotFound_whenAccountDoesNotExist() throws Exception {
        when(service.getByNumber("X")).thenThrow(new AccountNotFoundException("X"));

        mockMvc.perform(get("/accounts/X"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Aucun compte trouve avec le numero X"));
    }

    @Test
    void shouldReturnOk_whenListingAccounts() throws Exception {
        when(service.findAll()).thenReturn(List.of(
                new Account("C1", "Alice", BigDecimal.valueOf(100)),
                new Account("C2", "Bob", BigDecimal.valueOf(50))));

        mockMvc.perform(get("/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].number").value("C1"))
                .andExpect(jsonPath("$[1].holder").value("Bob"));

        verify(service).findAll();
    }

    @Test
    void shouldReturnOk_whenDepositIsValid() throws Exception {
        when(service.deposit(eq("C1"), any(BigDecimal.class)))
                .thenReturn(new Account("C1", "Alice", BigDecimal.valueOf(150)));

        mockMvc.perform(post("/accounts/C1/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":50}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(150));

        verify(service).deposit(eq("C1"), any(BigDecimal.class));
    }

    @Test
    void shouldReturnBadRequest_whenDepositAmountIsNotPositive() throws Exception {
        mockMvc.perform(post("/accounts/C1/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":0}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void shouldReturnOk_whenWithdrawIsValid() throws Exception {
        when(service.withdraw(eq("C1"), any(BigDecimal.class)))
                .thenReturn(new Account("C1", "Alice", BigDecimal.valueOf(60)));

        mockMvc.perform(post("/accounts/C1/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":40}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(60));

        verify(service).withdraw(eq("C1"), any(BigDecimal.class));
    }

    @Test
    void shouldReturnConflict_whenWithdrawHasInsufficientFunds() throws Exception {
        doThrow(new InsufficientFundsException("C1"))
                .when(service).withdraw(eq("C1"), any(BigDecimal.class));

        mockMvc.perform(post("/accounts/C1/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":1000}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    void shouldReturnOk_whenTransferIsValid() throws Exception {
        mockMvc.perform(post("/accounts/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"from\":\"C1\",\"to\":\"C2\",\"amount\":60}"))
                .andExpect(status().isOk());

        verify(service).transfer(eq("C1"), eq("C2"), any(BigDecimal.class));
    }

    @Test
    void shouldReturnConflict_whenTransferHasInsufficientFunds() throws Exception {
        doThrow(new InsufficientFundsException("C1"))
                .when(service).transfer(eq("C1"), eq("C2"), any(BigDecimal.class));

        mockMvc.perform(post("/accounts/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"from\":\"C1\",\"to\":\"C2\",\"amount\":1000}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }
}
