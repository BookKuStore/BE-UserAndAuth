package id.ac.ui.cs.advprog.bookku.bookku_userauth.service;

import id.ac.ui.cs.advprog.bookku.bookku_userauth.exceptions.*;
import id.ac.ui.cs.advprog.bookku.bookku_userauth.model.Account;
import id.ac.ui.cs.advprog.bookku.bookku_userauth.repository.AccountRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    private final JWTService jwtService;

    public List<Account> getAllAccount() {
         return accountRepository.findAll();
    }

    public List<Account> getAllAdminAccounts() {
        return accountRepository.findAllOptionalByRole("admin");
    }

    public Account getAccountFromToken(String token) {
        var account = jwtService.getAccountFromToken(token);

        if (account == null){
            throw new InvalidTokenException();
        }

        return account;
    }

}