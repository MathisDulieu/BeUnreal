package com.supinfo.beunreal_api_gateway.service;

import com.supinfo.beunreal_api_gateway.model.common.user.User;
import com.supinfo.beunreal_api_gateway.model.message.request.SendGroupMessageRequest;
import com.supinfo.beunreal_api_gateway.model.message.request.SendPrivateMessageRequest;
import com.supinfo.beunreal_api_gateway.model.message.request.UpdateMessageRequest;
import com.supinfo.beunreal_api_gateway.model.message.response.GetGroupConversationResponse;
import com.supinfo.beunreal_api_gateway.model.message.response.GetPrivateConversationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MessageService {
    public ResponseEntity<String> sendPrivateMessage(User authenticatedUser, String userId, SendPrivateMessageRequest request) {
        return null;
    }

    public ResponseEntity<GetPrivateConversationResponse> getPrivateConversation(User authenticatedUser, String userId) {
        return null;
    }

    public ResponseEntity<String> sendGroupMessage(User authenticatedUser, String groupId, SendGroupMessageRequest request) {
        return null;
    }

    public ResponseEntity<String> sendGroupMediaMessage(User authenticatedUser, String groupId, MultipartFile file) {
        return null;
    }

    public ResponseEntity<GetGroupConversationResponse> getGroupConversation(User authenticatedUser, String groupId) {
        return null;
    }

    public ResponseEntity<String> updateMessage(User authenticatedUser, String messageId, UpdateMessageRequest request) {
        return null;
    }

    public ResponseEntity<String> deleteMessage(User authenticatedUser, String messageId) {
        return null;
    }
}
