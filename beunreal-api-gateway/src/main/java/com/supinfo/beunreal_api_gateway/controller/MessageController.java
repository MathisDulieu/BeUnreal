package com.supinfo.beunreal_api_gateway.controller;

import com.supinfo.beunreal_api_gateway.model.common.user.User;
import com.supinfo.beunreal_api_gateway.model.message.request.SendGroupMessageRequest;
import com.supinfo.beunreal_api_gateway.model.message.request.SendPrivateMessageRequest;
import com.supinfo.beunreal_api_gateway.model.message.request.UpdateMessageRequest;
import com.supinfo.beunreal_api_gateway.model.message.response.GetGroupConversationResponse;
import com.supinfo.beunreal_api_gateway.model.message.response.GetPrivateConversationResponse;
import com.supinfo.beunreal_api_gateway.service.MessageService;
import com.supinfo.beunreal_api_gateway.swagger.MessageControllerDoc;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping("/messages/private/{userId}")
    @MessageControllerDoc.SendPrivateMessageDoc
    public ResponseEntity<String> sendPrivateMessage(
            @RequestBody SendPrivateMessageRequest request,
            @PathVariable String userId,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        return messageService.sendPrivateMessage(authenticatedUser, userId, request);
    }

    @GetMapping("/messages/private/{userId}")
    @MessageControllerDoc.GetPrivateConversationDoc
    public ResponseEntity<GetPrivateConversationResponse> getPrivateConversation(
            @PathVariable String userId,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        return messageService.getPrivateConversation(authenticatedUser, userId);
    }

    @PostMapping("/messages/group/{groupId}")
    @MessageControllerDoc.SendGroupMessageDoc
    public ResponseEntity<String> sendGroupMessage(
            @RequestBody SendGroupMessageRequest request,
            @PathVariable String groupId,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        return messageService.sendGroupMessage(authenticatedUser, groupId, request);
    }

    @GetMapping("/messages/group/{groupId}")
    @MessageControllerDoc.GetGroupConversationDoc
    public ResponseEntity<GetGroupConversationResponse> getGroupConversation(
            @PathVariable String groupId,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        return messageService.getGroupConversation(authenticatedUser, groupId);
    }

    @PutMapping("/messages/{messageId}")
    @MessageControllerDoc.UpdateMessageDoc
    public ResponseEntity<String> updateMessage(
            @RequestBody UpdateMessageRequest request,
            @PathVariable String messageId,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        return messageService.updateMessage(authenticatedUser, messageId, request);
    }

    @DeleteMapping("/messages/{messageId}")
    @MessageControllerDoc.DeleteMessageDoc
    public ResponseEntity<String> deleteMessage(
            @PathVariable String messageId,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        return messageService.deleteMessage(authenticatedUser, messageId);
    }

}
