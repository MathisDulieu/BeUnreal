package com.supinfo.beunreal_api_gateway.dao;

import com.supinfo.beunreal_api_gateway.model.common.user.User;
import com.supinfo.beunreal_api_gateway.model.user.response.model.FriendResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

@Component
@RequiredArgsConstructor
public class UserDao {

    private final MongoTemplate mongoTemplate;

    private static final String USER_COLLECTION = "users";

    public Optional<User> findById(String userId) {
        return Optional.ofNullable(mongoTemplate.findById(userId, User.class, USER_COLLECTION));
    }

    public boolean isUsernameAlreadyUsed(String username) {
        return mongoTemplate.exists(new Query(Criteria.where("username").is(username)), USER_COLLECTION);
    }

    public boolean isEmailAlreadyUsed(String email) {
        return mongoTemplate.exists(new Query(Criteria.where("email").is(email)), USER_COLLECTION);
    }

    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(mongoTemplate.findOne(new Query(Criteria.where("email").is(email)), User.class, USER_COLLECTION));
    }


    public boolean exists(String userId) {
        if (isNull(userId)) {
            return false;
        }

        return mongoTemplate.exists(
                new Query(Criteria.where("_id").is(userId)),
                User.class,
                USER_COLLECTION
        );
    }

    public List<User> findAllOrderedByUsernameAsc() {
        Query query = new Query();
        query.with(Sort.by(Sort.Direction.ASC, "username"));
        query.limit(5);

        return mongoTemplate.find(query, User.class, USER_COLLECTION);
    }

    public List<User> findByUsernameStartingWith(String prefix, int limit) {
        Query query = new Query(Criteria.where("username").regex("^" + prefix, "i"));
        query.with(Sort.by(Sort.Direction.ASC, "username"));
        query.limit(limit);

        return mongoTemplate.find(query, User.class, USER_COLLECTION);
    }

    public List<User> findByUsernameStartingWith(String prefix) {
        return findByUsernameStartingWith(prefix, 5);
    }

    public long countByUsernameStartingWith(String prefix) {
        Query query = new Query(Criteria.where("username").regex("^" + prefix, "i"));

        return mongoTemplate.count(query, USER_COLLECTION);
    }

    public long countAll() {
        return mongoTemplate.count(new Query(), USER_COLLECTION);
    }

    public List<FriendResponse> findLimitedFriends(List<String> friendIds) {
        Query query = new Query(Criteria.where("_id").in(friendIds));
        query.with(Sort.by(Sort.Direction.ASC, "username"));
        query.limit(5);

        List<User> friends = mongoTemplate.find(query, User.class, USER_COLLECTION);

        return friends.stream()
                .map(this::convertToFriendDTO)
                .toList();
    }

    public List<FriendResponse> findFriendsWithPrefix(List<String> friendIds, String prefix) {
        Query query = new Query(
                Criteria.where("_id").in(friendIds).and("username").regex("^" + prefix, "i")
        );
        query.with(Sort.by(Sort.Direction.ASC, "username"));

        List<User> friends = mongoTemplate.find(query, User.class, USER_COLLECTION);

        return friends.stream()
                .map(this::convertToFriendDTO)
                .toList();
    }

    public List<User> findAllByIds(List<String> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return Collections.emptyList();
        }

        Query query = new Query(Criteria.where("_id").in(userIds));
        return mongoTemplate.find(query, User.class, USER_COLLECTION);
    }

    private FriendResponse convertToFriendDTO(User user) {
        return FriendResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .profilePicture(user.getProfilePicture())
                .build();
    }

}