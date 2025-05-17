package com.supinfo.beunreal_api_gateway.controller;

import com.supinfo.beunreal_api_gateway.model.authentication.LoginRequest;
import com.supinfo.beunreal_api_gateway.model.authentication.RegisterRequest;
import com.supinfo.beunreal_api_gateway.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/auth/register")
    @AuthenticationControllerDoc.RegisterDoc
    public ResponseEntity<String> register(
            @RequestBody RegisterRequest request
    ) {
        return authenticationService.register(request);
    }

    @PostMapping("/auth/login")
    @AuthenticationControllerDoc.LoginDoc
    public ResponseEntity<String> login(
            @RequestBody LoginRequest request
    ) {
        return authenticationService.login(request);
    }

}
