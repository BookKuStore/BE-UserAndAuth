package id.ac.ui.cs.advprog.bookku.bookku_userauth.service;

import id.ac.ui.cs.advprog.bookku.bookku_userauth.exceptions.*;
import id.ac.ui.cs.advprog.bookku.bookku_userauth.dto.*;
import id.ac.ui.cs.advprog.bookku.bookku_userauth.model.Account;
import id.ac.ui.cs.advprog.bookku.bookku_userauth.repository.AccountRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final PasswordEncoder passwordEncoder;

    private final AccountRepository accountRepository;

    private final JWTService jwtService;

    public List<Account> getAllAccount() {
         return accountRepository.findAll();
    }

    public List<Account> getAllAdminAccounts() {
        return accountRepository.findAllOptionalByRole("admin");
    }

    public ChangePasswordResponse changePassword(String token, String newPassword) {

        jwtService.validateAccessToken(token);

        var account = getAccountFromToken(token);

        newPassword = passwordEncoder.encode(newPassword);

        account.setPassword(newPassword);

        accountRepository.save(account);

        return ChangePasswordResponse.builder()
            .message("Password changed successfully")
            .build();
    }

    public ChangeAttributeResponse changeAttribute(String token, HashMap<String, String> request) {
        var account = getAccountFromToken(token);

        for (var entry : request.entrySet()) {
            String attribute = entry.getKey().toString().toLowerCase();
            switch (attribute) {
                case "name":
                    account.setName(entry.getValue());
                    break;
                case "email":
                    account.setEmail(entry.getValue());
                    break;
                case "phone":
                    account.setPhone(entry.getValue());
                    break;
                case "gender":
                    account.setGender(entry.getValue());
                    break;
                case "bio":
                    account.setBio(entry.getValue());
                    break;
                case "profileurl":
                    account.setProfileUrl(entry.getValue());
                    break;
                case "cartid":
                    account.setCartId(Integer.parseInt(entry.getValue()));
                    break;
                case "historyid":
                    account.setHistoryId(Integer.parseInt(entry.getValue()));
                    break;
            }
        }

        accountRepository.save(account);

        return ChangeAttributeResponse.builder()
            .message("Attribute changed successfully")
            .build();
    }

    public Account getAccountFromToken(String token) {
        var account = jwtService.getAccountFromToken(token);

        if (account == null){
            throw new InvalidTokenException();
        }

        return account;
    }

}