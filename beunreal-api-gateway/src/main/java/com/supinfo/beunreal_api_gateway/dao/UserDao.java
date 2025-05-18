package com.supinfo.beunreal_api_gateway.dao;

import com.supinfo.beunreal_api_gateway.model.common.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserDao {

    private final MongoTemplate mongoTemplate;

    private static final String USER_COLLECTION = "users";

    public Optional<User> findById(String userId) {
        return Optional.ofNullable(mongoTemplate.findById(userId, User.class, USER_COLLECTION));
    }

}