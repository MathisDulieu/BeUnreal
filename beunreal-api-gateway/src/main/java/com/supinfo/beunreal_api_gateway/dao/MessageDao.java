package com.supinfo.beunreal_api_gateway.dao;

import com.supinfo.beunreal_api_gateway.model.common.message.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MessageDao {

    private final MongoTemplate mongoTemplate;

    private static final String MESSAGE_COLLECTION = "messages";

    public List<Message> findPrivateConversationMessages(String user1Id, String user2Id) {
        Query query = new Query();

        Criteria senderRecipientCriteria = new Criteria().orOperator(
                Criteria.where("senderId").is(user1Id).and("recipientId").is(user2Id),
                Criteria.where("senderId").is(user2Id).and("recipientId").is(user1Id)
        );

        query.addCriteria(senderRecipientCriteria);
        query.addCriteria(Criteria.where("groupId").exists(false));

        query.with(Sort.by(Sort.Direction.DESC, "sentAt"));

        return mongoTemplate.find(query, Message.class, MESSAGE_COLLECTION);
    }

}
