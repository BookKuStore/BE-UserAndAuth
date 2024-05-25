package id.ac.ui.cs.advprog.bookku.bookku_userauth.controller;

import id.ac.ui.cs.advprog.bookku.bookku_userauth.dto.AuthorizeResponse;
import id.ac.ui.cs.advprog.bookku.bookku_userauth.dto.AuthorizeRequest;
import id.ac.ui.cs.advprog.bookku.bookku_userauth.dto.RefreshRequest;
import id.ac.ui.cs.advprog.bookku.bookku_userauth.dto.RefreshResponse;
import id.ac.ui.cs.advprog.bookku.bookku_userauth.service.AuthorizationService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/authorization")
@RequiredArgsConstructor
public class AuthorizationController {

    private final AuthorizationService authorizationService;

    @PostMapping("/authorize")
    public ResponseEntity<AuthorizeResponse> authorize(
            @RequestBody AuthorizeRequest request,
            @RequestHeader(name="Authorization") String accessToken
    ) {
        request.setRole(request.getRole().toUpperCase());
        return ResponseEntity.ok(authorizationService.authorize(request, accessToken));
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponse> refresh(@RequestBody RefreshRequest request) {
        return ResponseEntity.ok(authorizationService.refresh(request));
    }

}
