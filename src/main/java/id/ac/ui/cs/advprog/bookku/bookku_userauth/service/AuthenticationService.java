package id.ac.ui.cs.advprog.bookku.bookku_userauth.service;

import id.ac.ui.cs.advprog.bookku.bookku_userauth.exceptions.*;
import id.ac.ui.cs.advprog.bookku.bookku_userauth.model.Account;
import id.ac.ui.cs.advprog.bookku.bookku_userauth.model.RefreshToken;
import id.ac.ui.cs.advprog.bookku.bookku_userauth.dto.LoginRequest;
import id.ac.ui.cs.advprog.bookku.bookku_userauth.dto.LoginResponse;
import id.ac.ui.cs.advprog.bookku.bookku_userauth.dto.RegisterRequest;
import id.ac.ui.cs.advprog.bookku.bookku_userauth.dto.RegisterResponse;
import id.ac.ui.cs.advprog.bookku.bookku_userauth.repository.AccountRepository;
import id.ac.ui.cs.advprog.bookku.bookku_userauth.repository.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JWTService jwtService;

    public LoginResponse login(LoginRequest request) {

        // Check blank inputs
        if (request.getUsername().isBlank() || request.getPassword().isBlank()) {
            throw new BlankInputException();
        }

        if (usernameContainsSpace(request.getUsername())) {
            throw new UsernameContainsSpaceException();
        }

        Account account = getAccountByUsername(request.getUsername());
        if (account == null) {
            throw new AccountNotFoundException();
        }

        if (passwordDoesNotMatch(request.getPassword(), account.getPassword())){
            throw new WrongPasswordException();
        }

        jwtService.deleteRefreshTokenByAccount(account);

        var accessToken = jwtService.generateAccessToken(account);
        var refreshToken = jwtService.generateRefreshToken(account);

        refreshTokenRepository.save(
            RefreshToken.builder()
                .token(refreshToken)
                .account(account)
                .build()
            );

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public RegisterResponse register(RegisterRequest request) {

        // Check blank inputs
        if (request.getUsername().isBlank() || request.getPassword().isBlank() || request.getName().isBlank() || request.getEmail().isBlank() || request.getPhone().isBlank()) {
            throw new BlankInputException();
        }

        if (usernameContainsSpace(request.getUsername())) {
            throw new UsernameContainsSpaceException();
        }

        if (accountAlreadyExist(request.getUsername())) {
            throw new UsernameAlreadyExistException();
        }

        var newAccount = Account.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .build();
        newAccount.setCartId(0);
        newAccount.setHistoryId(0);
        newAccount.setBio("Hello there! I'm using Bookku.");
        newAccount.setGender("Not specified");
        newAccount.setBirthdate("Not specified");
        newAccount.setProfileUrl("https://cdn.iconscout.com/icon/free/png-256/avatar-370-456322.png");
        accountRepository.save(newAccount);

        var accessToken = jwtService.generateAccessToken(newAccount);
        var refreshToken = jwtService.generateRefreshToken(newAccount);

        refreshTokenRepository.save(RefreshToken.builder()
                .token(refreshToken)
                .account(newAccount)
                .build());

        return RegisterResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private Account getAccountByUsername(String username){
        return accountRepository
        .findByUsername(username)
        .orElse(null);
    }

    private boolean accountAlreadyExist(String username) {
        return getAccountByUsername(username) != null;
    }

    private boolean usernameContainsSpace(String usernameInput){
        return usernameInput.contains(" ");
    }

    private boolean passwordDoesNotMatch(String passwordInput, String accountPassword){
        return !passwordEncoder.matches(passwordInput, accountPassword);
    }

}
