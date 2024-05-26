package id.ac.ui.cs.advprog.bookku.bookku_userauth.service;

import id.ac.ui.cs.advprog.bookku.bookku_userauth.exceptions.*;

import id.ac.ui.cs.advprog.bookku.bookku_userauth.dto.AuthorizeRequest;
import id.ac.ui.cs.advprog.bookku.bookku_userauth.dto.AuthorizeResponse;
import id.ac.ui.cs.advprog.bookku.bookku_userauth.dto.RefreshRequest;
import id.ac.ui.cs.advprog.bookku.bookku_userauth.dto.RefreshResponse;
import id.ac.ui.cs.advprog.bookku.bookku_userauth.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthorizationService {

    private final AccountRepository accountRepository;
    private final JWTService jwtService;

    public AuthorizeResponse authorize(AuthorizeRequest request, String accessToken) {

        validateAccess(request, accessToken);
        
        String tokenRole = jwtService.extractRole(accessToken).toUpperCase();
        if (!tokenRole.equals("ADMIN")) {
            validateAuthorizationRequest(request, accessToken);
        }

        return AuthorizeResponse.builder()
            .authorized(true)
            .message("Access granted")
            .build();
    }

    public RefreshResponse refresh(RefreshRequest request) {
        try {
            String refreshToken = request.getRefreshToken();

            validateRefreshToken(refreshToken);
            
            var username = jwtService.extractUsername(refreshToken);

            var account = accountRepository.findByUsername(username).orElse(null);

            if (account == null) {
                throw new UnauthorizedAccessException();
            }

            var newAccessToken = jwtService.generateAccessToken(account);

            return RefreshResponse.builder()
                .accessToken(newAccessToken)
                .build();
        } catch (ExpiredTokenException e) {
            var username = jwtService.extractUsername(request.getRefreshToken());
            var account = accountRepository.findByUsername(username).orElse(null);

            jwtService.deleteRefreshTokenByAccount(account);

            throw new ExpiredTokenException();
        }
    }

    private void validateRefreshToken(String refreshToken) {
        jwtService.validateRefreshToken(refreshToken);
    }

    private void validateAuthorizationRequest(AuthorizeRequest request, String accessToken) {
        
        String roleValidation = request.getRole();
        String roleToValidate = jwtService.extractRole(accessToken);

        if (isRoleNotAuthorized(roleToValidate, roleValidation)) {
            throw new UnauthorizedAccessException();
        }
    
    }

    private void validateAccess(AuthorizeRequest request, String accessToken) {
        
        jwtService.validateAccessToken(accessToken);

        var account = accountRepository.findByUsername(jwtService.extractUsername(accessToken)).orElse(null);
        if (account == null) {
            throw new AccountNotFoundException();
        }

    }

    private boolean isRoleNotAuthorized(String tokenRole, String role) {
        return !tokenRole.equals(role);
    }

}