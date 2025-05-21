package com.supinfo.beunreal_user_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supinfo.beunreal_user_service.model.KafkaMessage;
import com.supinfo.beunreal_user_service.service.FriendService;
import com.supinfo.beunreal_user_service.service.GroupService;
import com.supinfo.beunreal_user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class Consumer {

    @Autowired
    private ObjectMapper objectMapper;

    private final UserService userService;
    private final GroupService groupService;
    private final FriendService friendService;

    @KafkaListener(topics = "user-events", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeAuthenticationEvents(
            @Payload String messageJson,
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset,
            Acknowledgment acknowledgment) {

        try {
            log.info("JSON message received from user-service topic [key: {}, partition: {}, offset: {}]", key, partition, offset);

            KafkaMessage kafkaMessage = objectMapper.readValue(messageJson, KafkaMessage.class);

            handleOperation(key, kafkaMessage);

            acknowledgment.acknowledge();
        } catch (Exception e) {
            log.error("Error processing message: {}", e.getMessage(), e);
            acknowledgment.acknowledge();
        }
    }

    private void handleOperation(String operationKey, KafkaMessage kafkaMessage) {
        log.info("Processing operation: {}", operationKey);

        switch (operationKey) {
            case "updateAuthenticatedUserInfo":
                userService.updateAuthenticatedUserInfo(kafkaMessage);
                break;
            case "deleteAuthenticatedUserInfo":
                userService.deleteAuthenticatedUserInfo(kafkaMessage);
                break;
            case "addFriend":
                friendService.addFriend(kafkaMessage);
                break;
            case "deleteFriend":
                friendService.deleteFriend(kafkaMessage);
                break;
            case "acceptFriendRequest":
                friendService.acceptFriendRequest(kafkaMessage);
                break;
            case "rejectFriendRequest":
                friendService.rejectFriendRequest(kafkaMessage);
                break;
            case "createGroup":
                groupService.createGroup(kafkaMessage);
                break;
            case "updateGroup":
                groupService.updateGroup(kafkaMessage);
                break;
            case "deleteGroup":
                groupService.deleteGroup(kafkaMessage);
                break;
            case "removeUserFromGroup":
                groupService.removeUserFromGroup(kafkaMessage);
                break;
            case "addUserToGroup":
                groupService.addUserToGroup(kafkaMessage);
                break;
            case "rejectGroupInvitation":
                groupService.rejectGroupInvitation(kafkaMessage);
                break;
            case "acceptGroupInvitation":
                groupService.acceptGroupInvitation(kafkaMessage);
                break;
            case "cancelGroupInvitation":
                groupService.cancelGroupInvitation(kafkaMessage);
                break;
            case "cancelFriendRequest":
                friendService.cancelFriendRequest(kafkaMessage);
                break;
            default:
                log.warn("Unknown operation: {}", operationKey);
        }
    }
}