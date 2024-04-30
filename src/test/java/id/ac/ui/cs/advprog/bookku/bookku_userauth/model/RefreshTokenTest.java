package id.ac.ui.cs.advprog.bookku.bookku_userauth.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RefreshTokenTest {

    @Test
    public void testRefreshTokenModel() {
        // Create a sample account
        Account account = new Account();
        account.setId(1); // Set account id
        account.setUsername("testuser");
        account.setPassword("testpassword");
        account.setRole("user");
        account.setName("Test User");
        account.setEmail("test@example.com");
        account.setPhone("1234567890");

        // Create a refresh token associated with the account
        RefreshToken refreshToken = RefreshToken.builder()
                .account(account)
                .token("testtoken")
                .build();

        // Test getters
        assertEquals(account, refreshToken.getAccount());
        assertEquals("testtoken", refreshToken.getToken());

        // Test setters
        Account newAccount = new Account();
        newAccount.setId(2); // Set new account id
        refreshToken.setAccount(newAccount);
        assertEquals(newAccount, refreshToken.getAccount());

        refreshToken.setToken("newtoken");
        assertEquals("newtoken", refreshToken.getToken());
    }
    
}