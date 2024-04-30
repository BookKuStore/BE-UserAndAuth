package id.ac.ui.cs.advprog.bookku.bookku_userauth.repository;

import id.ac.ui.cs.advprog.bookku.bookku_userauth.model.Account;
import id.ac.ui.cs.advprog.bookku.bookku_userauth.model.RefreshToken;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RefreshTokenRepositoryTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Test
    public void testExistsByToken() {
        when(refreshTokenRepository.existsByToken("testtoken")).thenReturn(true);

        boolean exists = refreshTokenRepository.existsByToken("testtoken");
        assertTrue(exists);
    }

    @Test
    public void testFindByAccount() {
        Account account = new Account();
        account.setId(1); // Set account id

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setAccount(account);
        when(refreshTokenRepository.findByAccount(account)).thenReturn(Optional.of(refreshToken));

        Optional<RefreshToken> foundToken = refreshTokenRepository.findByAccount(account);
        assertTrue(foundToken.isPresent());
        assertEquals(account, foundToken.get().getAccount());
    }

}