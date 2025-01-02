package com.dailytasktracker.controller;

import com.dailytasktracker.model.Account;
import com.dailytasktracker.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AccountControllerTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    private MockMvc mockMvc;
    private Account account;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();

        account = new Account();
        account.setId(1L);
    }

    @Test
    void testCreateAccount() throws Exception {
        when(accountService.createAccount(account)).thenReturn(account);

        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(account)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));

        verify(accountService, times(1)).createAccount(account);
    }

    @Test
    void testGetAccountById() throws Exception {
        when(accountService.getAccountById(1L)).thenReturn(java.util.Optional.of(account));

        mockMvc.perform(get("/api/accounts/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(accountService, times(1)).getAccountById(1L);
    }

    @Test
    void testUpdateAccount() throws Exception {
        when(accountService.updateAccount(1L, account)).thenReturn(account);

        mockMvc.perform(put("/api/accounts/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(account)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(accountService, times(1)).updateAccount(1L, account);
    }

    @Test
    void testDeleteAccount() throws Exception {
        doNothing().when(accountService).deleteAccount(1L);

        mockMvc.perform(delete("/api/accounts/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(accountService, times(1)).deleteAccount(1L);
    }
}
