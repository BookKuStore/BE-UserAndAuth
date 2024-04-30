package id.ac.ui.cs.advprog.bookku.bookku_userauth.repository;

import id.ac.ui.cs.advprog.bookku.bookku_userauth.model.Account;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    Optional<Account> findByUsername(String username);

    @Transactional
    void deleteByUsername(String username);
}