package com.supinfo.beunreal_api_gateway.controller;

import com.supinfo.beunreal_api_gateway.model.authentication.LoginRequest;
import com.supinfo.beunreal_api_gateway.model.authentication.RegisterRequest;
import com.supinfo.beunreal_api_gateway.service.AuthenticationService;
import com.supinfo.beunreal_api_gateway.swagger.AuthenticationControllerDoc;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @AuthenticationControllerDoc.RegisterDoc
    public ResponseEntity<String> register(
            @RequestBody RegisterRequest request,
            HttpServletRequest httpRequest
    ) {
        return authenticationService.register(request, httpRequest);
    }

    @PostMapping("/login")
    @AuthenticationControllerDoc.LoginDoc
    public ResponseEntity<String> login(
            @RequestBody LoginRequest request,
            HttpServletRequest httpRequest
    ) {
        return authenticationService.login(request, httpRequest);
    }

}
