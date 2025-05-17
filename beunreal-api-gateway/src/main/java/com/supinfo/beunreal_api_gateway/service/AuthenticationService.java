package com.supinfo.beunreal_api_gateway.service;

import com.supinfo.beunreal_api_gateway.model.authentication.LoginRequest;
import com.supinfo.beunreal_api_gateway.model.authentication.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    public ResponseEntity<String> register(RegisterRequest request) {
        return null;
    }

    public ResponseEntity<String> login(LoginRequest request) {
        return null;
    }
}