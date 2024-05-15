package id.ac.ui.cs.advprog.bookku.bookku_userauth.service;

import id.ac.ui.cs.advprog.bookku.bookku_userauth.dto.LoginRequest;
import id.ac.ui.cs.advprog.bookku.bookku_userauth.dto.LoginResponse;
import id.ac.ui.cs.advprog.bookku.bookku_userauth.dto.RegisterRequest;
import id.ac.ui.cs.advprog.bookku.bookku_userauth.dto.RegisterResponse;
import id.ac.ui.cs.advprog.bookku.bookku_userauth.exceptions.*;
import id.ac.ui.cs.advprog.bookku.bookku_userauth.model.Account;
import id.ac.ui.cs.advprog.bookku.bookku_userauth.model.RefreshToken;
import id.ac.ui.cs.advprog.bookku.bookku_userauth.repository.AccountRepository;
import id.ac.ui.cs.advprog.bookku.bookku_userauth.repository.RefreshTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    @Mock
    private JWTService jwtService;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoginSuccessful() {
        LoginRequest loginRequest = new LoginRequest("username", "password");
        Account account = new Account();
        account.setUsername("username");
        account.setPassword("encodedPassword");

        when(accountRepository.findByUsername("username")).thenReturn(Optional.of(account));
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);
        when(jwtService.generateAccessToken(account)).thenReturn("accessToken");
        when(jwtService.generateRefreshToken(account)).thenReturn("refreshToken");

        LoginResponse response = authenticationService.login(loginRequest);

        assertEquals("accessToken", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());
        verify(jwtService).deleteRefreshTokenByAccount(account);
        verify(refreshTokenRepository).save(any(RefreshToken.class));
    }

    @Test
    void testLoginUsernameContainsSpace() {
        LoginRequest loginRequest = new LoginRequest("user name", "password");

        assertThrows(UsernameContainsSpaceException.class, () -> authenticationService.login(loginRequest));
    }

    @Test
    void testLoginAccountNotFound() {
        LoginRequest loginRequest = new LoginRequest("username", "password");

        when(accountRepository.findByUsername("username")).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> authenticationService.login(loginRequest));
    }

    @Test
    void testLoginWrongPassword() {
        LoginRequest loginRequest = new LoginRequest("username", "password");
        Account account = new Account();
        account.setUsername("username");
        account.setPassword("encodedPassword");

        when(accountRepository.findByUsername("username")).thenReturn(Optional.of(account));
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(false);

        assertThrows(WrongPasswordException.class, () -> authenticationService.login(loginRequest));
    }

    @Test
    void testRegisterSuccessful() {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "role", "name", "email", "phone");
        when(accountRepository.findByUsername("username")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(jwtService.generateAccessToken(any(Account.class))).thenReturn("accessToken");
        when(jwtService.generateRefreshToken(any(Account.class))).thenReturn("refreshToken");

        RegisterResponse response = authenticationService.register(registerRequest);

        assertEquals("accessToken", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());

        ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(accountCaptor.capture());
        Account savedAccount = accountCaptor.getValue();
        assertEquals("username", savedAccount.getUsername());
        assertEquals("encodedPassword", savedAccount.getPassword());
        assertEquals("role", savedAccount.getRole());
        assertEquals("name", savedAccount.getName());
        assertEquals("email", savedAccount.getEmail());
        assertEquals("phone", savedAccount.getPhone());

        verify(refreshTokenRepository).save(any(RefreshToken.class));
    }

    @Test
    void testRegisterUsernameContainsSpace() {
        RegisterRequest registerRequest = new RegisterRequest("user name", "password", "role", "name", "email", "phone");

        assertThrows(UsernameContainsSpaceException.class, () -> authenticationService.register(registerRequest));
    }

    @Test
    void testRegisterUsernameAlreadyExists() {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "role", "name", "email", "phone");
        when(accountRepository.findByUsername("username")).thenReturn(Optional.of(new Account()));

        assertThrows(UsernameAlreadyExistException.class, () -> authenticationService.register(registerRequest));
    }
}
