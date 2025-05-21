package com.supinfo.beunreal_api_gateway.controller;

import com.supinfo.beunreal_api_gateway.model.common.user.User;
import com.supinfo.beunreal_api_gateway.model.message.request.SendGroupMessageRequest;
import com.supinfo.beunreal_api_gateway.model.message.request.SendPrivateMessageRequest;
import com.supinfo.beunreal_api_gateway.model.message.request.UpdateMessageRequest;
import com.supinfo.beunreal_api_gateway.model.message.response.GetGroupConversationResponse;
import com.supinfo.beunreal_api_gateway.model.message.response.GetPrivateConversationResponse;
import com.supinfo.beunreal_api_gateway.service.MessageService;
import com.supinfo.beunreal_api_gateway.swagger.MessageControllerDoc;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping("/private/messages/private/{userId}")
    @MessageControllerDoc.SendPrivateMessageDoc
    public ResponseEntity<String> sendPrivateMessage(
            @RequestBody SendPrivateMessageRequest request,
            @PathVariable String userId,
            @AuthenticationPrincipal User authenticatedUser,
            HttpServletRequest httpRequest
    ) {
        return messageService.sendPrivateMessage(authenticatedUser, userId, request, httpRequest);
    }

    @GetMapping("/private/messages/private/{userId}")
    @MessageControllerDoc.GetPrivateConversationDoc
    public ResponseEntity<GetPrivateConversationResponse> getPrivateConversation(
            @PathVariable String userId,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        return messageService.getPrivateConversation(authenticatedUser, userId);
    }

    @PostMapping("/private/messages/group/{groupId}")
    @MessageControllerDoc.SendGroupMessageDoc
    public ResponseEntity<String> sendGroupMessage(
            @RequestBody SendGroupMessageRequest request,
            @PathVariable String groupId,
            @AuthenticationPrincipal User authenticatedUser,
            HttpServletRequest httpRequest
    ) {
        return messageService.sendGroupMessage(authenticatedUser, groupId, request, httpRequest);
    }

    @GetMapping("/private/messages/group/{groupId}")
    @MessageControllerDoc.GetGroupConversationDoc
    public ResponseEntity<GetGroupConversationResponse> getGroupConversation(
            @PathVariable String groupId,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        return messageService.getGroupConversation(authenticatedUser, groupId);
    }


}
