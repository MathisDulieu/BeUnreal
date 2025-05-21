package com.supinfo.beunreal_api_gateway.dao;

import com.supinfo.beunreal_api_gateway.model.common.friendRequest.FriendRequest;
import com.supinfo.beunreal_api_gateway.model.common.friendRequest.FriendRequestStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FriendRequestDao {

    private final MongoTemplate mongoTemplate;

    private static final String FRIEND_REQUEST_COLLECTION = "friendRequests";

    public Optional<FriendRequest> findById(String id) {
        return Optional.ofNullable(mongoTemplate.findById(id, FriendRequest.class, FRIEND_REQUEST_COLLECTION));
    }

    public List<FriendRequest> findBySenderId(String senderId) {
        Query query = new Query(Criteria.where("senderId").is(senderId));
        return mongoTemplate.find(query, FriendRequest.class, FRIEND_REQUEST_COLLECTION);
    }

    public List<FriendRequest> findByRecipientIdAndStatus(String recipientId, FriendRequestStatus status) {
        Query query = new Query(Criteria.where("recipientId").is(recipientId)
                .and("status").is(status));
        return mongoTemplate.find(query, FriendRequest.class, FRIEND_REQUEST_COLLECTION);
    }

}
