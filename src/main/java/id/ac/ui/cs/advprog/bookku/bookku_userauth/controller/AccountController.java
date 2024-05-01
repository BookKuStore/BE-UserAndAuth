package id.ac.ui.cs.advprog.bookku.bookku_userauth.controller;

import id.ac.ui.cs.advprog.bookku.bookku_userauth.model.Account;
import id.ac.ui.cs.advprog.bookku.bookku_userauth.service.AccountService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;


@RestController()
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/all")
    public ResponseEntity<List<Account>> getAllAccount() {
        List<Account> listAccount = accountService.getAllAccount();
        return ResponseEntity.ok(listAccount);
    }

    @GetMapping("/get-all-admin")
    public ResponseEntity<List<Account>> getAllAdminAccounts() {
        return ResponseEntity.ok(accountService.getAllAdminAccounts());
    }
    
    @GetMapping("/seed-admin")
    public ResponseEntity<String> seedAdmin() {

        List<Account> adminAccounts = accountService.getAllAdminAccounts();
        int counts = adminAccounts.size();
        if (counts == 5) {
            return ResponseEntity.ok("Admin accounts already seeded");
        } else {
            for (int i = 0; i < 5 - counts; i++) {
                Account admin = Account.builder()
                        .username(String.format("admin%d", i+1))
                        .password(String.format("admin%d", i+1))
                        .name(String.format("Admin %d", i+1))
                        .email("admin@admin.com")
                        .phone("123")
                        .role("admin")
                        .build();
                accountService.registerAccount(admin);
            }

            return ResponseEntity.ok("Admin accounts seeded");
        }
    }
    
}