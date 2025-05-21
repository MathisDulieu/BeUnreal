package com.supinfo.beunreal_message_service.dao;

import com.supinfo.beunreal_message_service.model.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MessageDao {

    private final MongoTemplate mongoTemplate;

    private static final String MESSAGE_COLLECTION = "messages";

    public Optional<Message> findById(String messageId) {
        return Optional.ofNullable(mongoTemplate.findById(messageId, Message.class, MESSAGE_COLLECTION));
    }

    public void save(Message message) {
        mongoTemplate.save(message, MESSAGE_COLLECTION);
    }

}