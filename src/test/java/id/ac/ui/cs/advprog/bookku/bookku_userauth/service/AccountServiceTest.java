package id.ac.ui.cs.advprog.bookku.bookku_userauth.service;

import id.ac.ui.cs.advprog.bookku.bookku_userauth.exceptions.InvalidTokenException;
import id.ac.ui.cs.advprog.bookku.bookku_userauth.model.Account;
import id.ac.ui.cs.advprog.bookku.bookku_userauth.repository.AccountRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private JWTService jwtService;

    @InjectMocks
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllAccount() {
        // Arrange
        List<Account> accounts = new ArrayList<>();
        when(accountRepository.findAll()).thenReturn(accounts);

        // Act
        List<Account> result = accountService.getAllAccount();

        // Assert
        assertEquals(accounts, result);
        verify(accountRepository).findAll();
    }

    @Test
    void testGetAllAdminAccounts() {
        // Arrange
        List<Account> adminAccounts = new ArrayList<>();
        when(accountRepository.findAllOptionalByRole("admin")).thenReturn(adminAccounts);

        // Act
        List<Account> result = accountService.getAllAdminAccounts();

        // Assert
        assertEquals(adminAccounts, result);
        verify(accountRepository).findAllOptionalByRole("admin");
    }

    @Test
    void testGetAccountFromToken_ValidToken() {
        // Arrange
        String validToken = "valid_token";
        Account expectedAccount = new Account();
        when(jwtService.getAccountFromToken(validToken)).thenReturn(expectedAccount);

        // Act
        Account result = accountService.getAccountFromToken(validToken);

        // Assert
        assertEquals(expectedAccount, result);
        verify(jwtService).getAccountFromToken(validToken);
    }

    @Test
    void testGetAccountFromToken_InvalidToken() {
        // Arrange
        String invalidToken = "invalid_token";
        when(jwtService.getAccountFromToken(invalidToken)).thenReturn(null);

        // Act & Assert
        assertThrows(InvalidTokenException.class, () -> accountService.getAccountFromToken(invalidToken));
        verify(jwtService).getAccountFromToken(invalidToken);
    }
}