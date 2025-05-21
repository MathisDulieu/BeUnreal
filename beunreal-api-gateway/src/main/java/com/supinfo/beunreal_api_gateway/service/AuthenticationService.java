package com.supinfo.beunreal_api_gateway.service;

import com.supinfo.beunreal_api_gateway.Producer;
import com.supinfo.beunreal_api_gateway.dao.UserDao;
import com.supinfo.beunreal_api_gateway.model.authentication.LoginRequest;
import com.supinfo.beunreal_api_gateway.model.authentication.RegisterRequest;
import com.supinfo.beunreal_api_gateway.model.common.kafka.KafkaMessage;
import com.supinfo.beunreal_api_gateway.model.common.user.User;
import com.supinfo.beunreal_api_gateway.utils.UserUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final Producer producer;
    private final JwtTokenService jwtTokenService;
    private final PasswordEncoder passwordEncoder;
    private final UserUtils userUtils;
    private final UserDao userDao;

    public ResponseEntity<String> register(RegisterRequest request, HttpServletRequest httpRequest) {
        String error = userUtils.getRegisterValidationError(request);
        if (!isNull(error)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        Map<String, String> kafkaRequest = Map.of(
                "username", request.getUsername(),
                "email", request.getEmail(),
                "password", passwordEncoder.encode(request.getPassword())
        );

        KafkaMessage kafkaMessage = producer.buildKafkaMessage(null, httpRequest, kafkaRequest);

        producer.send(kafkaMessage, "auth-events", "register");

        return ResponseEntity.status(HttpStatus.OK).body("Registration successful! You can now log in to your account.");
    }

    public ResponseEntity<String> login(LoginRequest request) {
        Optional<User> optionalUser = userDao.findByEmail(request.getEmail());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found. Please check your email address or register for a new account.");
        }

        if (!passwordEncoder.matches(request.getPassword(), optionalUser.get().getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid password. Please try again.");
        }

        return ResponseEntity.status(HttpStatus.OK).body(jwtTokenService.generateToken(optionalUser.get().getId()));
    }

}