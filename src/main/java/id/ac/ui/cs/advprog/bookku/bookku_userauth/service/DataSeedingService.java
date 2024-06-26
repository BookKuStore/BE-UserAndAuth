package id.ac.ui.cs.advprog.bookku.bookku_userauth.service;

import id.ac.ui.cs.advprog.bookku.bookku_userauth.model.Account;
import id.ac.ui.cs.advprog.bookku.bookku_userauth.repository.AccountRepository;
import id.ac.ui.cs.advprog.bookku.bookku_userauth.repository.RefreshTokenRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.ArrayList;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeedingService {

    private final AccountRepository accountRepository;

    private final RefreshTokenRepository refreshTokenRepository;

    private final PasswordEncoder passwordEncoder;

    private List<Account> adminData() {

        List<Account> adminList = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            Account admin = Account.builder()
                    .username("admin" + i)
                    .password(passwordEncoder.encode("admin" + i))
                    .role("ADMIN")
                    .name("Admin " + i)
                    .email("admin" + i + "@bookku.com")
                    .phone("123321" + i)
                    .build();
            adminList.add(admin);
        }

        return adminList;
    }

    @PostConstruct
    public void init() {
        refreshTokenRepository.deleteAll();
        accountRepository.deleteAll();

        List<Account> adminList = adminData();
        accountRepository.saveAll(adminList);
    }
    
}
