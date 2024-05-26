package id.ac.ui.cs.advprog.bookku.bookku_userauth.controller;

import id.ac.ui.cs.advprog.bookku.bookku_userauth.dto.ChangePasswordResponse;
import id.ac.ui.cs.advprog.bookku.bookku_userauth.dto.ChangePasswordRequest;
import id.ac.ui.cs.advprog.bookku.bookku_userauth.dto.ChangeAttributeResponse;
import id.ac.ui.cs.advprog.bookku.bookku_userauth.model.Account;
import id.ac.ui.cs.advprog.bookku.bookku_userauth.service.AccountService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



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

    @PostMapping("/change-password")
    public ResponseEntity<ChangePasswordResponse> postMethodName(@RequestHeader(name = "Authorization") String accessToken, @RequestBody ChangePasswordRequest request) {
        return ResponseEntity.ok(accountService.changePassword(accessToken, request.getNewPassword()));
    }

    @PostMapping("/change-attribute")
    public ResponseEntity<ChangeAttributeResponse> postMethodName(@RequestHeader(name = "Authorization") String accessToken, @RequestBody HashMap<String, String> request) {
        return ResponseEntity.ok(accountService.changeAttribute(accessToken, request));
    }
    
    
}