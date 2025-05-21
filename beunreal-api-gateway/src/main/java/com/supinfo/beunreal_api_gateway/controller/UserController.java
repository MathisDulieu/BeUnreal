package com.supinfo.beunreal_api_gateway.controller;

import com.supinfo.beunreal_api_gateway.model.common.user.User;
import com.supinfo.beunreal_api_gateway.model.user.request.CreateGroupRequest;
import com.supinfo.beunreal_api_gateway.model.user.request.UpdateAuthenticatedUserInfoRequest;
import com.supinfo.beunreal_api_gateway.model.user.request.UpdateGroupRequest;
import com.supinfo.beunreal_api_gateway.model.user.response.*;
import com.supinfo.beunreal_api_gateway.service.UserService;
import com.supinfo.beunreal_api_gateway.swagger.UserControllerDoc;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/private/user/{userId}")
    @UserControllerDoc.GetUserInfoDoc
    public ResponseEntity<GetUserInfoResponse> getUserInfo(
            @PathVariable String userId,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        return userService.getUserInfo(authenticatedUser, userId);
    }

    @PutMapping("/private/user")
    @UserControllerDoc.UpdateAuthenticatedUserInfoDoc
    public ResponseEntity<String> updateAuthenticatedUserInfo(
            @RequestBody UpdateAuthenticatedUserInfoRequest request,
            @AuthenticationPrincipal User authenticatedUser,
            HttpServletRequest httpRequest
    ) {
        return userService.updateAuthenticatedUserInfo(authenticatedUser, request, httpRequest);
    }

    @DeleteMapping("/private/user")
    @UserControllerDoc.DeleteAuthenticatedUserDoc
    public ResponseEntity<String> deleteAuthenticatedUser(
            @AuthenticationPrincipal User authenticatedUser,
            HttpServletRequest httpRequest
    ) {
        return userService.deleteAuthenticatedUserInfo(authenticatedUser, httpRequest);
    }

    @GetMapping("/private/users/search/{prefix}")
    @UserControllerDoc.SearchUsersDoc
    public ResponseEntity<SearchUsersResponse> searchUsers(
            @PathVariable String prefix,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        return userService.searchUsers(authenticatedUser, prefix);
    }

    @PostMapping("/private/users/add/{friendId}")
    @UserControllerDoc.AddFriendDoc
    public ResponseEntity<String> addFriend(
            @PathVariable String friendId,
            @AuthenticationPrincipal User authenticatedUser,
            HttpServletRequest httpRequest
    ) {
        return userService.addFriend(authenticatedUser, friendId, httpRequest);
    }

    @DeleteMapping("/private/users/friends/{friendId}")
    @UserControllerDoc.DeleteFriendDoc
    public ResponseEntity<String> deleteFriend(
            @PathVariable String friendId,
            @AuthenticationPrincipal User authenticatedUser,
            HttpServletRequest httpRequest
    ) {
        return userService.deleteFriend(authenticatedUser, friendId, httpRequest);
    }

    @GetMapping("/private/users/friends/requests/sent")
    @UserControllerDoc.GetSentFriendsRequestsDoc
    public ResponseEntity<GetSentFriendsRequestsResponse> getSentFriendsRequests(
            @AuthenticationPrincipal User authenticatedUser
    ) {
        return userService.getSentFriendsRequests(authenticatedUser);
    }

    @GetMapping("/private/users/friends/requests/received")
    @UserControllerDoc.GetReceivedFriendsRequestsDoc
    public ResponseEntity<GetReceivedFriendsRequestsResponse> getReceivedFriendsRequests(
            @AuthenticationPrincipal User authenticatedUser
    ) {
        return userService.getReceivedFriendsRequests(authenticatedUser);
    }

    @PutMapping("/private/users/friends/requests/{requestId}/accept")
    @UserControllerDoc.AcceptFriendRequestDoc
    public ResponseEntity<String> acceptFriendRequest(
            @PathVariable String requestId,
            @AuthenticationPrincipal User authenticatedUser,
            HttpServletRequest httpRequest
    ) {
        return userService.acceptFriendRequest(authenticatedUser, requestId, httpRequest);
    }

    @PutMapping("/private/users/friends/requests/{requestId}/reject")
    @UserControllerDoc.RejectFriendRequestDoc
    public ResponseEntity<String> rejectFriendRequest(
            @PathVariable String requestId,
            @AuthenticationPrincipal User authenticatedUser,
            HttpServletRequest httpRequest
    ) {
        return userService.rejectFriendRequest(authenticatedUser, requestId, httpRequest);
    }

    @PutMapping("/private/users/friends/requests/{requestId}/cancel")
    @UserControllerDoc.CancelFriendRequestDoc
    public ResponseEntity<String> cancelFriendRequest(
            @PathVariable String requestId,
            @AuthenticationPrincipal User authenticatedUser,
            HttpServletRequest httpRequest
    ) {
        return userService.cancelFriendRequest(authenticatedUser, requestId, httpRequest);
    }

    @GetMapping("/private/users/friends/{prefix}")
    @UserControllerDoc.GetFriendsDoc
    public ResponseEntity<GetFriendsResponse> getFriends(
            @PathVariable String prefix,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        return userService.getFriends(authenticatedUser, prefix);
    }

    @PostMapping("/private/group")
    @UserControllerDoc.CreateGroupDoc
    public ResponseEntity<String> createGroup(
            @RequestBody CreateGroupRequest request,
            @AuthenticationPrincipal User authenticatedUser,
            HttpServletRequest httpRequest
    ) {
        return userService.createGroup(authenticatedUser, request, httpRequest);
    }

    @PutMapping("/private/group/{groupId}")
    @UserControllerDoc.UpdateGroupDoc
    public ResponseEntity<String> updateGroup(
            @PathVariable String groupId,
            @RequestBody UpdateGroupRequest request,
            @AuthenticationPrincipal User authenticatedUser,
            HttpServletRequest httpRequest
    ) {
        return userService.updateGroup(authenticatedUser, groupId, request, httpRequest);
    }

    @DeleteMapping("/private/group/{groupId}")
    @UserControllerDoc.DeleteGroupDoc
    public ResponseEntity<String> deleteGroup(
            @PathVariable String groupId,
            @AuthenticationPrincipal User authenticatedUser,
            HttpServletRequest httpRequest
    ) {
        return userService.deleteGroup(authenticatedUser, groupId, httpRequest);
    }

    @GetMapping("/private/group/{groupId}")
    @UserControllerDoc.GetGroupInfoDoc
    public ResponseEntity<GetGroupInfoResponse> getGroupInfo(
            @PathVariable String groupId,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        return userService.getGroupInfo(authenticatedUser, groupId);
    }

    @PutMapping("/private/group/{groupId}/members/remove/{userId}")
    @UserControllerDoc.RemoveUserFromGroupDoc
    public ResponseEntity<String> removeUserFromGroup(
            @PathVariable String groupId,
            @PathVariable String userId,
            @AuthenticationPrincipal User authenticatedUser,
            HttpServletRequest httpRequest
    ) {
        return userService.removeUserFromGroup(authenticatedUser, groupId, userId, httpRequest);
    }

    @PutMapping("/private/group/{groupId}/members/add/{userId}")
    @UserControllerDoc.AddUserToGroupDoc
    public ResponseEntity<String> addUserToGroup(
            @PathVariable String groupId,
            @PathVariable String userId,
            @AuthenticationPrincipal User authenticatedUser,
            HttpServletRequest httpRequest
    ) {
        return userService.addUserToGroup(authenticatedUser, groupId, userId, httpRequest);
    }

    @GetMapping("/private/group/{groupId}/invitations")
    @UserControllerDoc.GetGroupInvitationsDoc
    public ResponseEntity<GetGroupInvitationsResponse> getGroupInvitations(
            @PathVariable String groupId,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        return userService.getGroupInvitations(authenticatedUser, groupId);
    }

    @GetMapping("/private/user/group/invitations")
    @UserControllerDoc.GetUserGroupInvitationsDoc
    public ResponseEntity<GetUserGroupInvitationsResponse> getUserGroupInvitations(
            @AuthenticationPrincipal User authenticatedUser
    ) {
        return userService.getUserGroupInvitations(authenticatedUser);
    }

    @PutMapping("/private/group/{groupId}/invitation/reject")
    @UserControllerDoc.RejectGroupInvitationDoc
    public ResponseEntity<String> rejectGroupInvitation(
            @PathVariable String groupId,
            @AuthenticationPrincipal User authenticatedUser,
            HttpServletRequest httpRequest
    ) {
        return userService.rejectGroupInvitation(authenticatedUser, groupId, httpRequest);
    }

    @PutMapping("/private/group/{groupId}/invitation/accept")
    @UserControllerDoc.AcceptGroupInvitationDoc
    public ResponseEntity<String> acceptGroupInvitation(
            @PathVariable String groupId,
            @AuthenticationPrincipal User authenticatedUser,
            HttpServletRequest httpRequest
    ) {
        return userService.acceptGroupInvitation(authenticatedUser, groupId, httpRequest);
    }

    @PutMapping("/private/group/{groupId}/invitation/{invitationId}/cancel")
    @UserControllerDoc.CancelGroupInvitationDoc
    public ResponseEntity<String> cancelGroupInvitation(
            @PathVariable String groupId,
            @PathVariable String invitationId,
            @AuthenticationPrincipal User authenticatedUser,
            HttpServletRequest httpRequest
    ) {
        return userService.cancelGroupInvitation(authenticatedUser, groupId, invitationId, httpRequest);
    }

}
