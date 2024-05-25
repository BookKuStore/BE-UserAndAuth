package id.ac.ui.cs.advprog.bookku.bookku_userauth.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AccountTest {

    @Test
    public void testAccountModel() {
        Account account = Account.builder()
                .username("testuser")
                .password("testpassword")
                .role("user")
                .name("Test User")
                .email("test@example.com")
                .phone("1234567890")
                .build();

        assertEquals("testuser", account.getUsername());
        assertEquals("testpassword", account.getPassword());
        assertEquals("user", account.getRole());
        assertEquals("Test User", account.getName());
        assertEquals("test@example.com", account.getEmail());
        assertEquals("1234567890", account.getPhone());

        account.setUsername("newuser");
        assertEquals("newuser", account.getUsername());

        account.setPassword("newpassword");
        assertEquals("newpassword", account.getPassword());

        account.setRole("admin");
        assertEquals("admin", account.getRole());

        account.setName("New User");
        assertEquals("New User", account.getName());

        account.setEmail("new@example.com");
        assertEquals("new@example.com", account.getEmail());

        account.setPhone("0987654321");
        assertEquals("0987654321", account.getPhone());
    }

}