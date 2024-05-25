package id.ac.ui.cs.advprog.bookku.bookku_userauth.repository;

import id.ac.ui.cs.advprog.bookku.bookku_userauth.model.Account;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    Optional<Account> findByUsername(String username);

    List<Account> findAllOptionalByRole(String role);

    @Transactional
    void deleteByUsername(String username);
}