package com.supinfo.beunreal_auth_service.service;

import com.supinfo.beunreal_auth_service.UuidProvider;
import com.supinfo.beunreal_auth_service.configuration.EnvConfiguration;
import com.supinfo.beunreal_auth_service.dao.UserDao;
import com.supinfo.beunreal_auth_service.model.KafkaMessage;
import com.supinfo.beunreal_auth_service.model.User;
import com.supinfo.beunreal_auth_service.model.UserStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UuidProvider uuidProvider;
    private final EnvConfiguration envConfiguration;
    private final UserDao userDao;

    public void processRegister(KafkaMessage kafkaMessage) {
        Map<String, String> request = kafkaMessage.getRequest();
        String email = request.get("email");
        String encodedPassword = request.get("password");
        String username = request.get("username");

        log.info("Starting to process register request for user with email: {}", email);

        User user = User.builder()
                .id(uuidProvider.generateUuid())
                .email(email)
                .password(encodedPassword)
                .username(username)
                .profilePicture(envConfiguration.getDefaultProfileImage())
                .status(UserStatus.ONLINE)
                .build();

        userDao.save(user);
    }

}
