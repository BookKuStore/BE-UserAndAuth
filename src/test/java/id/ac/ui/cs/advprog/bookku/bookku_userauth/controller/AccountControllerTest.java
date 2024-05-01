package id.ac.ui.cs.advprog.bookku.bookku_userauth.controller;

import id.ac.ui.cs.advprog.bookku.bookku_userauth.model.Account;
import id.ac.ui.cs.advprog.bookku.bookku_userauth.service.AccountService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AccountControllerTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllAccount() {
        // Arrange
        List<Account> accounts = new ArrayList<>();
        when(accountService.getAllAccount()).thenReturn(accounts);

        // Act
        ResponseEntity<List<Account>> response = accountController.getAllAccount();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(accounts, response.getBody());
        verify(accountService).getAllAccount();
    }

    @Test
    void testGetAllAdminAccounts() {
        // Arrange
        List<Account> adminAccounts = new ArrayList<>();
        when(accountService.getAllAdminAccounts()).thenReturn(adminAccounts);

        // Act
        ResponseEntity<List<Account>> response = accountController.getAllAdminAccounts();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(adminAccounts, response.getBody());
        verify(accountService).getAllAdminAccounts();
    }

    @Test
    void testSeedAdmin_NoExistingAdminAccounts() {
        // Arrange
        List<Account> emptyAdminAccounts = new ArrayList<>();
        when(accountService.getAllAdminAccounts()).thenReturn(emptyAdminAccounts);

        // Act
        ResponseEntity<String> response = accountController.seedAdmin();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Admin accounts seeded", response.getBody());
        verify(accountService, times(5)).registerAccount(any());
    }

}
