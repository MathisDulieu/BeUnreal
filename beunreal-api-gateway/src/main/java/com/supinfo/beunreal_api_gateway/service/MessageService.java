package com.supinfo.beunreal_api_gateway.service;

import com.supinfo.beunreal_api_gateway.Producer;
import com.supinfo.beunreal_api_gateway.dao.MessageDao;
import com.supinfo.beunreal_api_gateway.dao.UserDao;
import com.supinfo.beunreal_api_gateway.model.common.kafka.KafkaMessage;
import com.supinfo.beunreal_api_gateway.model.common.message.Message;
import com.supinfo.beunreal_api_gateway.model.common.user.User;
import com.supinfo.beunreal_api_gateway.model.message.request.SendGroupMessageRequest;
import com.supinfo.beunreal_api_gateway.model.message.request.SendPrivateMessageRequest;
import com.supinfo.beunreal_api_gateway.model.message.request.UpdateMessageRequest;
import com.supinfo.beunreal_api_gateway.model.message.response.GetGroupConversationResponse;
import com.supinfo.beunreal_api_gateway.model.message.response.GetPrivateConversationResponse;
import com.supinfo.beunreal_api_gateway.model.message.response.model.PrivateMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final Producer producer;
    private final UserDao userDao;
    private final MessageDao messageDao;

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

        PrivateMessage privateMessage = PrivateMessage.builder().build();

        return ResponseEntity.ok(GetPrivateConversationResponse.builder()
                        .messages(Collections.emptyList())
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
        return null;
    }

}
