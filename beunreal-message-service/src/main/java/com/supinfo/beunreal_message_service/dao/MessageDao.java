package com.supinfo.beunreal_message_service.dao;

import com.supinfo.beunreal_message_service.model.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageDao {

    private final MongoTemplate mongoTemplate;

    private static final String MESSAGE_COLLECTION = "messages";

    public void save(Message message) {
        mongoTemplate.save(message, MESSAGE_COLLECTION);
    }

}