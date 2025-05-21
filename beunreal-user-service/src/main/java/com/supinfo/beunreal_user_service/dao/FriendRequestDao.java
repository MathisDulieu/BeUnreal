package com.supinfo.beunreal_user_service.dao;

import com.supinfo.beunreal_user_service.model.FriendRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FriendRequestDao {

    private final MongoTemplate mongoTemplate;

    private static final String FRIEND_REQUEST_COLLECTION = "friendRequests";

    public Optional<FriendRequest> findById(String id) {
        return Optional.ofNullable(mongoTemplate.findById(id, FriendRequest.class, FRIEND_REQUEST_COLLECTION));
    }

    public void save(FriendRequest friendRequest) {
        mongoTemplate.save(friendRequest, FRIEND_REQUEST_COLLECTION);
    }

    public void delete(FriendRequest friendRequest) {
        mongoTemplate.remove(friendRequest, FRIEND_REQUEST_COLLECTION);
    }
}
