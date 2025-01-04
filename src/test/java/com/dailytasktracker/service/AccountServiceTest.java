/*
package com.dailytasktracker.service;

import com.dailytasktracker.model.Account;
import com.dailytasktracker.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    private Account account;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        account = new Account();
        account.setId(1L);
    }

    @Test
    void testCreateAccount() {
        when(accountRepository.save(account)).thenReturn(account);
        Account createdAccount = accountService.createAccount(account);

        assertNotNull(createdAccount);
        assertEquals(1L, createdAccount.getId());
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void testGetAccountById() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        Optional<Account> foundAccount = accountService.getAccountById(1L);

        assertTrue(foundAccount.isPresent());
        assertEquals(1L, foundAccount.get().getId());
        verify(accountRepository, times(1)).findById(1L);
    }

    @Test
    @Disabled
    void testUpdateAccount() {
        account.setId(1L);
        account.setId(2L);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(account)).thenReturn(account);

        Account updatedAccount = accountService.updateAccount(1L, account);

        assertNotNull(updatedAccount);
        assertEquals(1L, updatedAccount.getId());
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void testDeleteAccount() {
        doNothing().when(accountRepository).deleteById(1L);
        accountService.deleteAccount(1L);

        verify(accountRepository, times(1)).deleteById(1L);
    }
}
*/
