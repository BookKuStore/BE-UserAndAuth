package id.ac.ui.cs.advprog.bookku.bookku_userauth.repository;

import id.ac.ui.cs.advprog.bookku.bookku_userauth.model.Account;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountRepositoryTest {

    @Mock
    private AccountRepository accountRepository;

    @Test
    public void testFindByUsername() {
        Account account = new Account();
        account.setUsername("testuser");
        when(accountRepository.findByUsername("testuser")).thenReturn(Optional.of(account));

        Optional<Account> foundAccount = accountRepository.findByUsername("testuser");
        assertEquals("testuser", foundAccount.orElseThrow().getUsername());
    }

    @Test
    public void testDeleteByUsername() {
        doNothing().when(accountRepository).deleteByUsername("testuser");

        accountRepository.deleteByUsername("testuser");
        verify(accountRepository, times(1)).deleteByUsername("testuser");
    }

}