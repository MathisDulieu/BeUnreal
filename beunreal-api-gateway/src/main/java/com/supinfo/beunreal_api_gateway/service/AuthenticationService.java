package com.supinfo.beunreal_api_gateway.service;

import com.supinfo.beunreal_api_gateway.model.authentication.LoginRequest;
import com.supinfo.beunreal_api_gateway.model.authentication.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtTokenService jwtTokenService;

    public ResponseEntity<String> register(RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body("Registration successful! You can now log in to your account.");
    }

    public ResponseEntity<String> login(LoginRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body("token");
    }

}