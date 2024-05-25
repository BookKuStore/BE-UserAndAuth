package id.ac.ui.cs.advprog.bookku.bookku_userauth.service;

import id.ac.ui.cs.advprog.bookku.bookku_userauth.dto.AuthorizeRequest;
import id.ac.ui.cs.advprog.bookku.bookku_userauth.dto.AuthorizeResponse;
import id.ac.ui.cs.advprog.bookku.bookku_userauth.dto.RefreshRequest;
import id.ac.ui.cs.advprog.bookku.bookku_userauth.dto.RefreshResponse;
import id.ac.ui.cs.advprog.bookku.bookku_userauth.exceptions.*;
import id.ac.ui.cs.advprog.bookku.bookku_userauth.model.Account;
import id.ac.ui.cs.advprog.bookku.bookku_userauth.repository.AccountRepository;
import id.ac.ui.cs.advprog.bookku.bookku_userauth.repository.RefreshTokenRepository;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AuthorizationServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private JWTService jwtService;

    @InjectMocks
    private AuthorizationService authorizationService;

    @Test
    public void testAuthorize_Success() {
        String accessToken = "validAccessToken";
        AuthorizeRequest request = new AuthorizeRequest("ROLE_USER");

        Account account = new Account();
        account.setUsername("testUser");
        account.setRole("ROLE_USER");

        when(jwtService.validateAccessToken(accessToken)).thenReturn(true);
        when(jwtService.extractUsername(accessToken)).thenReturn("testUser");
        when(accountRepository.findByUsername("testUser")).thenReturn(java.util.Optional.of(account));
        when(jwtService.extractRole(accessToken)).thenReturn("ROLE_USER");

        AuthorizeResponse response = authorizationService.authorize(request, accessToken);

        assertTrue(response.isAuthorized());
        assertEquals("Access granted", response.getMessage());
    }

    @Test
    public void testAuthorize_AccountNotFound() {
        String accessToken = "validAccessToken";
        AuthorizeRequest request = new AuthorizeRequest("ROLE_USER");

        when(jwtService.validateAccessToken(accessToken)).thenReturn(true);
        when(jwtService.extractUsername(accessToken)).thenReturn("testUser");
        when(accountRepository.findByUsername("testUser")).thenReturn(java.util.Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> {
            authorizationService.authorize(request, accessToken);
        });
    }

    @Test
    public void testAuthorize_UnauthorizedAccess() {
        String accessToken = "validAccessToken";
        AuthorizeRequest request = new AuthorizeRequest("ROLE_ADMIN");

        Account account = new Account();
        account.setUsername("testUser");
        account.setRole("ROLE_USER");

        when(jwtService.validateAccessToken(accessToken)).thenReturn(true);
        when(jwtService.extractUsername(accessToken)).thenReturn("testUser");
        when(accountRepository.findByUsername("testUser")).thenReturn(java.util.Optional.of(account));
        when(jwtService.extractRole(accessToken)).thenReturn("ROLE_USER");

        assertThrows(UnauthorizedAccessException.class, () -> {
            authorizationService.authorize(request, accessToken);
        });
    }

    @Test
    public void testRefresh_Success() {
        String refreshToken = "validRefreshToken";
        RefreshRequest request = new RefreshRequest(refreshToken);

        Account account = new Account();
        account.setUsername("testUser");

        when(jwtService.validateRefreshToken(refreshToken)).thenReturn(true);
        when(jwtService.extractUsername(refreshToken)).thenReturn("testUser");
        when(accountRepository.findByUsername("testUser")).thenReturn(java.util.Optional.of(account));
        when(jwtService.generateAccessToken(account)).thenReturn("newAccessToken");

        RefreshResponse response = authorizationService.refresh(request);

        assertEquals("newAccessToken", response.getAccessToken());
    }

    @Test
    public void testRefresh_ExpiredToken() {
        String refreshToken = "expiredRefreshToken";
        RefreshRequest request = new RefreshRequest(refreshToken);

        doThrow(new ExpiredTokenException()).when(jwtService).validateRefreshToken(refreshToken);

        assertThrows(ExpiredTokenException.class, () -> {
            authorizationService.refresh(request);
        });

        verify(refreshTokenRepository, times(1)).deleteByToken(refreshToken);
    }

    @Test
    public void testRefresh_UnauthorizedAccess() {
        String refreshToken = "validRefreshToken";
        RefreshRequest request = new RefreshRequest(refreshToken);

        when(jwtService.validateRefreshToken(refreshToken)).thenReturn(true);
        when(jwtService.extractUsername(refreshToken)).thenReturn("testUser");
        when(accountRepository.findByUsername("testUser")).thenReturn(java.util.Optional.empty());

        assertThrows(UnauthorizedAccessException.class, () -> {
            authorizationService.refresh(request);
        });
    }
}