package id.ac.ui.cs.advprog.bookku.bookku_userauth.service;

import id.ac.ui.cs.advprog.bookku.bookku_userauth.exceptions.*;
import id.ac.ui.cs.advprog.bookku.bookku_userauth.model.Account;
import id.ac.ui.cs.advprog.bookku.bookku_userauth.repository.AccountRepository;
import id.ac.ui.cs.advprog.bookku.bookku_userauth.repository.RefreshTokenRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class JWTServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private JWTService jwtService;

    private Account testAccount;

    @BeforeEach
    public void setUp() {
        testAccount = new Account();
        testAccount.setId(1);
        testAccount.setUsername("testuser");
        testAccount.setRole("user");
    }

    @Test
    public void testGenerateAccessToken_ShouldReturnToken() {
        String token = jwtService.generateAccessToken(testAccount);
        System.out.println(token);
        assertNotNull(token);
    }

    @Test
    public void testGenerateRefreshToken_ShouldReturnToken() {
        String token = jwtService.generateRefreshToken(testAccount);
        assertNotNull(token);
    }

    @Test
    public void testValidateAccessTokenInCorrectFormat() {
        String token = jwtService.generateAccessToken(testAccount);
        int n = token.split("[.]").length;
        assertTrue(n == 3);
    }

    @Test
    public void testValidateRefreshTokenInCorrectFormat() {
        String token = jwtService.generateRefreshToken(testAccount);
        int n = token.split("[.]").length;
        assertTrue(n == 3);
    }

    @Test
    public void testValidateRefreshToken_ShouldThrowInvalidTokenException() {
        String token = jwtService.generateRefreshToken(testAccount);
        assertThrows(InvalidTokenException.class, () -> jwtService.validateRefreshToken(token + "a"));
    }

    @Test
    public void testValidateAccessToken_ShouldThrowInvalidTokenException() {
        String token = jwtService.generateAccessToken(testAccount);
        assertThrows(InvalidTokenException.class, () -> jwtService.validateAccessToken(token + "a"));
    }

    @Test 
    public void testExpiredToken_ShouldThrowExpiredTokenException() {
        String expiredTokenExample = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoidXNlciIsInVzZXJuYW1lIjoidGVzdHVzZXIiLCJzdWIiOiIxIiwiaWF0IjoxNzE0NTY3OTU0LCJleHAiOjE3MTQ1NDI3NDR9.AWgRv9lpTuM_HMCNmvFhfx3VEgdUjlE5am8FyDMFMEM";
        assertThrows(ExpiredTokenException.class, () -> jwtService.validateAccessToken(expiredTokenExample));
    }

    @Test
    public void testExtractUsername_ShouldReturnUsername() {
        String token = jwtService.generateAccessToken(testAccount);
        assertEquals("testuser", jwtService.extractUsername(token));
    }

    @Test
    public void testExtractRole_ShouldReturnRole() {
        String token = jwtService.generateAccessToken(testAccount);
        assertEquals("user", jwtService.extractRole(token));
    }

    @Test
    public void testExtractId_ShouldReturnId() {
        String token = jwtService.generateAccessToken(testAccount);
        assertEquals("1", jwtService.extractId(token));
    }

}
