package com.matheushdas.restfulapi.controller;

import com.matheushdas.restfulapi.dto.auth.LoginRequest;
import com.matheushdas.restfulapi.dto.auth.LoginResponse;
import com.matheushdas.restfulapi.dto.auth.RegisterRequest;
import com.matheushdas.restfulapi.dto.auth.RegisterResponse;
import com.matheushdas.restfulapi.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticates a user returns a Access Token")
    public ResponseEntity login(@RequestBody @Valid LoginRequest login) {
        LoginResponse response = authService.login(login);

        return response == null ?
                ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!") :
                ResponseEntity.ok(response);

    }

    @PutMapping("/refresh/{username}")
    @Operation(summary = "Refresh Access Token for authenticated user")
    public ResponseEntity login(@PathVariable String username,
                                @RequestHeader("Authorization") String refreshToken) {
        LoginResponse response = authService.refreshToken(username, refreshToken);

        return response == null ?
                ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!") :
                ResponseEntity.ok(response);

    }

    @PostMapping("/register")
    @Operation(summary = "Create a new User")
    public ResponseEntity<RegisterResponse> register(@RequestBody @Valid RegisterRequest register) {
        return ResponseEntity.ok(authService.register(register));
    }
}
