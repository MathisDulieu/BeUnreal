package com.supinfo.beunreal_message_service.service;

import com.supinfo.beunreal_message_service.UuidProvider;
import com.supinfo.beunreal_message_service.dao.MessageDao;
import com.supinfo.beunreal_message_service.model.KafkaMessage;
import com.supinfo.beunreal_message_service.model.Message;
import com.supinfo.beunreal_message_service.model.MessageStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageDao messageDao;
    private final UuidProvider uuidProvider;

    public void sendPrivateMessage(KafkaMessage kafkaMessage) {
        Map<String, String> request = kafkaMessage.getRequest();
        String content = request.get("content");
        String userId = request.get("userId");

        Message message = Message.builder()
                .id(uuidProvider.generateUuid())
                .content(content)
                .deliveredAt(LocalDateTime.now())
                .sentAt(LocalDateTime.now())
                .status(MessageStatus.DELIVERED)
                .recipientId(userId)
                .senderId(kafkaMessage.getAuthenticatedUser().getId())
                .build();

        messageDao.save(message);

        log.info("Message sent to user with id : " + userId);
    }

    public void sendGroupMessage(KafkaMessage kafkaMessage) {
        Map<String, String> request = kafkaMessage.getRequest();
        String content = request.get("content");
        String groupId = request.get("groupId");

        Message message = Message.builder()
                .id(uuidProvider.generateUuid())
                .content(content)
                .deliveredAt(LocalDateTime.now())
                .sentAt(LocalDateTime.now())
                .status(MessageStatus.DELIVERED)
                .groupId(groupId)
                .senderId(kafkaMessage.getAuthenticatedUser().getId())
                .build();

        messageDao.save(message);

        log.info("Message sent to group with id : " + groupId);
    }

}
