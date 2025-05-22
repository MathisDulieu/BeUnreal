package com.supinfo.beunreal_media_service.dao;

import com.supinfo.beunreal_media_service.model.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MessageDao {

    private final MongoTemplate mongoTemplate;

    private static final String MESSAGE_COLLECTION = "messages";

    public void save(Message message) {
        mongoTemplate.save(message, MESSAGE_COLLECTION);
    }

}