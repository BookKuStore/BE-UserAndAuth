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

    @GetMapping("/get-account")
    public ResponseEntity<Account> getAccountByToken(@RequestHeader(name = "Authorization") String accessToken) {
        return ResponseEntity.ok(accountService.getAccountFromToken(accessToken));
    }
    
}