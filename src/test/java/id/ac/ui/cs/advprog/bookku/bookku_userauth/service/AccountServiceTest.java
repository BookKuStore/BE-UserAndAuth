package id.ac.ui.cs.advprog.bookku.bookku_userauth.service;

import id.ac.ui.cs.advprog.bookku.bookku_userauth.dto.ChangeAttributeResponse;
import id.ac.ui.cs.advprog.bookku.bookku_userauth.dto.ChangePasswordResponse;
import id.ac.ui.cs.advprog.bookku.bookku_userauth.exceptions.InvalidTokenException;
import id.ac.ui.cs.advprog.bookku.bookku_userauth.model.Account;
import id.ac.ui.cs.advprog.bookku.bookku_userauth.repository.AccountRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AccountServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private JWTService jwtService;

    @InjectMocks
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Account newAccount = Account.builder()
                .username("test")
                .password("test")
                .role("user")
                .name("Test")
                .email("test@gmail.com")
                .phone("1234567890")
                .build();
        newAccount.setCartId(newAccount.getId());
        newAccount.setHistoryId(newAccount.getId());

        when(accountRepository.save(newAccount)).thenReturn(newAccount);
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

    @Test
    void testChangePassword_ValidToken() {
        // Arrange
        String token = "valid_token";
        String newPassword = "new_password";
        Account account = new Account();
        String encodedPassword = "encoded_password";

        when(jwtService.getAccountFromToken(token)).thenReturn(account);
        when(passwordEncoder.encode(newPassword)).thenReturn(encodedPassword);

        // Act
        ChangePasswordResponse response = accountService.changePassword(token, newPassword);

        // Assert
        assertNotNull(response);
        assertEquals("Password changed successfully", response.getMessage());
        assertEquals(encodedPassword, account.getPassword());
        verify(accountRepository).save(account);
    }

    @Test
    void testChangePassword_InvalidToken() {
        // Arrange
        String token = "invalid_token";
        String newPassword = "new_password";

        when(jwtService.validateAccessToken(token)).thenThrow(InvalidTokenException.class);

        // Act & Assert
        assertThrows(InvalidTokenException.class, () -> accountService.changePassword(token, newPassword));
        verify(accountRepository, never()).save(any(Account.class));
    }

        @Test
    void testChangeAttribute_ValidToken() {
        // Arrange
        String token = "valid_token";
        HashMap<String, String> attributes = new HashMap<>();
        attributes.put("name", "New Name");
        attributes.put("email", "newemail@example.com");
        attributes.put("phone", "1234567890");
        attributes.put("cartId", "1");
        attributes.put("historyId", "1");
        attributes.put("bio", "Hello there! I'm using Bookku.");
        attributes.put("gender", "Not specified");
        attributes.put("birthdate", "01/01/2001");
        Account account = new Account();

        when(jwtService.getAccountFromToken(token)).thenReturn(account);

        // Act
        ChangeAttributeResponse response = accountService.changeAttribute(token, attributes);

        // Assert
        assertNotNull(response);
        assertEquals("Attribute changed successfully", response.getMessage());
        assertEquals("New Name", account.getName());
        assertEquals("newemail@example.com", account.getEmail());
        assertEquals("1234567890", account.getPhone());
        assertEquals(1, account.getCartId());
        assertEquals(1, account.getHistoryId());
        assertEquals("Hello there! I'm using Bookku.", account.getBio());
        assertEquals("Not specified", account.getGender());
        assertEquals("01/01/2001", account.getBirthdate());
        
        verify(accountRepository).save(account);
    }

    @Test
    void testChangeAttribute_InvalidToken() {
        // Arrange
        String token = "invalid_token";
        HashMap<String, String> attributes = new HashMap<>();

        when(jwtService.getAccountFromToken(token)).thenReturn(null);

        // Act & Assert
        assertThrows(InvalidTokenException.class, () -> accountService.changeAttribute(token, attributes));
        verify(accountRepository, never()).save(any(Account.class));
    }

}