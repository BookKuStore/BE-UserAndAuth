package id.ac.ui.cs.advprog.bookku.bookku_userauth.controller;

import id.ac.ui.cs.advprog.bookku.bookku_userauth.dto.LoginRequest;
import id.ac.ui.cs.advprog.bookku.bookku_userauth.dto.LoginResponse;
import id.ac.ui.cs.advprog.bookku.bookku_userauth.dto.RegisterRequest;
import id.ac.ui.cs.advprog.bookku.bookku_userauth.dto.RegisterResponse;
import id.ac.ui.cs.advprog.bookku.bookku_userauth.service.AuthenticationService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/authentication")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request){
        return ResponseEntity.ok(authenticationService.login(request));
    }
    
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register (@RequestBody RegisterRequest request) {
        request.setRole(request.getRole().toUpperCase());
        return ResponseEntity.ok(authenticationService.register(request));
    }

}
