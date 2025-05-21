package com.supinfo.beunreal_user_service.service;

import com.supinfo.beunreal_user_service.dao.UserDao;
import com.supinfo.beunreal_user_service.model.KafkaMessage;
import com.supinfo.beunreal_user_service.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;

    public void updateAuthenticatedUserInfo(KafkaMessage kafkaMessage) {
        User authenticatedUser = kafkaMessage.getAuthenticatedUser();
        userDao.save(authenticatedUser);

        log.info("User with id : " + authenticatedUser.getId() + " has been updated successfully!");
    }

    public void deleteAuthenticatedUserInfo(KafkaMessage kafkaMessage) {
        User authenticatedUser = kafkaMessage.getAuthenticatedUser();
        userDao.delete(authenticatedUser);

        log.info("User with id : " + authenticatedUser.getId() + " has been deleted successfully!");
    }

}
