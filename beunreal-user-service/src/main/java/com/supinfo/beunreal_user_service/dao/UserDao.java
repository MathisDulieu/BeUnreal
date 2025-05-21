package com.supinfo.beunreal_user_service.dao;

import com.supinfo.beunreal_user_service.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserDao {

    private final MongoTemplate mongoTemplate;

    private static final String USER_COLLECTION = "users";

    public void save(User user) {
        mongoTemplate.save(user, USER_COLLECTION);
    }

    public void delete(User user) {
        mongoTemplate.remove(user, USER_COLLECTION);
    }

    public Optional<User> findById(String userId) {
        return Optional.ofNullable(mongoTemplate.findById(userId, User.class, USER_COLLECTION));
    }


}
