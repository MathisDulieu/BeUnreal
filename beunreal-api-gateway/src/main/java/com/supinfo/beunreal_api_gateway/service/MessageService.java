package com.supinfo.beunreal_api_gateway.service;

import com.supinfo.beunreal_api_gateway.Producer;
import com.supinfo.beunreal_api_gateway.dao.GroupDao;
import com.supinfo.beunreal_api_gateway.dao.MessageDao;
import com.supinfo.beunreal_api_gateway.dao.UserDao;
import com.supinfo.beunreal_api_gateway.model.common.group.Group;
import com.supinfo.beunreal_api_gateway.model.common.kafka.KafkaMessage;
import com.supinfo.beunreal_api_gateway.model.common.message.Message;
import com.supinfo.beunreal_api_gateway.model.common.user.User;
import com.supinfo.beunreal_api_gateway.model.message.request.SendGroupMessageRequest;
import com.supinfo.beunreal_api_gateway.model.message.request.SendPrivateMessageRequest;
import com.supinfo.beunreal_api_gateway.model.message.response.GetGroupConversationResponse;
import com.supinfo.beunreal_api_gateway.model.message.response.GetPrivateConversationResponse;
import com.supinfo.beunreal_api_gateway.model.message.response.model.GroupInfo;
import com.supinfo.beunreal_api_gateway.model.message.response.model.GroupMessage;
import com.supinfo.beunreal_api_gateway.model.message.response.model.PrivateMessage;
import com.supinfo.beunreal_api_gateway.model.user.response.model.FriendResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final Producer producer;
    private final UserDao userDao;
    private final MessageDao messageDao;
    private final GroupDao groupDao;

    public ResponseEntity<String> sendPrivateMessage(User authenticatedUser, String userId, SendPrivateMessageRequest request, HttpServletRequest httpRequest) {
        Map<String, String> kafkaRequest = Map.of(
                "content", request.getContent(),
                "userId", userId
        );

        KafkaMessage kafkaMessage = producer.buildKafkaMessage(authenticatedUser, httpRequest, kafkaRequest);
        producer.send(kafkaMessage, "message-events", "sendPrivateMessage");

        return ResponseEntity.status(HttpStatus.OK).body("Private message send successfully");
    }

    public ResponseEntity<GetPrivateConversationResponse> getPrivateConversation(User authenticatedUser, String userId) {
        Optional<User> recipient = userDao.findById(userId);
        if (recipient.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        String authenticatedUserId = authenticatedUser.getId();

        List<Message> messages = messageDao.findPrivateConversationMessages(authenticatedUserId, userId);

        if (messages.isEmpty()) {
            User recipientUser = recipient.get();
            FriendResponse friendResponse = FriendResponse.builder()
                    .id(recipientUser.getId())
                    .username(recipientUser.getUsername())
                    .profilePicture(recipientUser.getProfilePicture())
                    .status(recipientUser.getStatus())
                    .build();

            return ResponseEntity.ok(GetPrivateConversationResponse.builder()
                    .messages(Collections.emptyList())
                    .friend(friendResponse)
                    .build());
        }

        Optional<User> optionalReceiver = userDao.findById(messages.getFirst().getRecipientId());
        Optional<User> optionalSender = userDao.findById(messages.getFirst().getSenderId());

        if (optionalReceiver.isEmpty() || optionalSender.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        FriendResponse receiver = FriendResponse.builder()
                .id(optionalReceiver.get().getId())
                .username(optionalReceiver.get().getUsername())
                .status(optionalReceiver.get().getStatus())
                .profilePicture(optionalReceiver.get().getProfilePicture())
                .build();

        FriendResponse sender = FriendResponse.builder()
                .id(optionalSender.get().getId())
                .username(optionalSender.get().getUsername())
                .status(optionalSender.get().getStatus())
                .profilePicture(optionalSender.get().getProfilePicture())
                .build();

        List<PrivateMessage> privateMessages = messages.stream()
                .map(message -> PrivateMessage.builder()
                        .content(message.getContent())
                        .status(message.getStatus())
                        .sentAt(message.getSentAt())
                        .id(message.getId())
                        .mediaUrl(message.getMediaUrl())
                        .receiver(receiver)
                        .sender(sender)
                        .build())
                .collect(Collectors.toList());

        User recipientUser = recipient.get();
        FriendResponse friendResponse = FriendResponse.builder()
                .id(recipientUser.getId())
                .username(recipientUser.getUsername())
                .profilePicture(recipientUser.getProfilePicture())
                .status(recipientUser.getStatus())
                .build();

        return ResponseEntity.ok(GetPrivateConversationResponse.builder()
                .messages(privateMessages)
                .friend(friendResponse)
                .build());
    }

    public ResponseEntity<String> sendGroupMessage(User authenticatedUser, String groupId, SendGroupMessageRequest request, HttpServletRequest httpRequest) {
        Map<String, String> kafkaRequest = Map.of(
                "content", request.getContent(),
                "groupId", groupId
        );

        KafkaMessage kafkaMessage = producer.buildKafkaMessage(authenticatedUser, httpRequest, kafkaRequest);
        producer.send(kafkaMessage, "message-events", "sendGroupMessage");

        return ResponseEntity.status(HttpStatus.OK).body("Group message send successfully");
    }

    public ResponseEntity<GetGroupConversationResponse> getGroupConversation(User authenticatedUser, String groupId) {
        Optional<Group> optionalGroup = groupDao.findGroupById(groupId);
        if (optionalGroup.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Group group = optionalGroup.get();
        String authenticatedUserId = authenticatedUser.getId();

        if (!group.getMemberIds().contains(authenticatedUserId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        GroupInfo groupInfo = GroupInfo.builder()
                .id(group.getId())
                .name(group.getName())
                .groupPicture(group.getGroupPicture())
                .build();

        List<Message> messages = messageDao.findGroupConversationMessages(groupId);

        if (messages.isEmpty()) {
            return ResponseEntity.ok(GetGroupConversationResponse.builder()
                    .group(groupInfo)
                    .messages(Collections.emptyList())
                    .build());
        }

        Map<String, FriendResponse> userMap = new HashMap<>();

        for (Message message : messages) {
            if (!userMap.containsKey(message.getSenderId())) {
                Optional<User> optionalSender = userDao.findById(message.getSenderId());
                if (optionalSender.isPresent()) {
                    User sender = optionalSender.get();
                    FriendResponse friendResponse = FriendResponse.builder()
                            .id(sender.getId())
                            .username(sender.getUsername())
                            .status(sender.getStatus())
                            .profilePicture(sender.getProfilePicture())
                            .build();
                    userMap.put(sender.getId(), friendResponse);
                }
            }
        }

        List<GroupMessage> groupMessages = messages.stream()
                .map(message -> {
                    FriendResponse sender = userMap.getOrDefault(message.getSenderId(),
                            FriendResponse.builder().id(message.getSenderId()).username("Unknown User").build());

                    return GroupMessage.builder()
                            .id(message.getId())
                            .content(message.getContent())
                            .status(message.getStatus())
                            .sentAt(message.getSentAt())
                            .mediaUrl(message.getMediaUrl())
                            .sender(sender)
                            .build();
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(GetGroupConversationResponse.builder()
                .group(groupInfo)
                .messages(groupMessages)
                .build());
    }

}
