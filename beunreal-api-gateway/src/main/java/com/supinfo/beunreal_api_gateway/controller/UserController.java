package com.supinfo.beunreal_api_gateway.controller;

import com.supinfo.beunreal_api_gateway.model.common.user.User;
import com.supinfo.beunreal_api_gateway.model.user.request.CreateGroupRequest;
import com.supinfo.beunreal_api_gateway.model.user.request.UpdateAuthenticatedUserInfoRequest;
import com.supinfo.beunreal_api_gateway.model.user.request.UpdateGroupRequest;
import com.supinfo.beunreal_api_gateway.model.user.response.*;
import com.supinfo.beunreal_api_gateway.service.UserService;
import com.supinfo.beunreal_api_gateway.swagger.UserControllerDoc;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/user/{userId}")
    @UserControllerDoc.GetAuthenticatedUserInfoDoc
    public ResponseEntity<GetAuthenticatedUserInfoResponse> getAuthenticatedUserInfo(
            @PathVariable String userId,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        return userService.getAuthenticatedUserInfo(authenticatedUser, userId);
    }

    @PutMapping("/user")
    @UserControllerDoc.UpdateAuthenticatedUserInfoDoc
    public ResponseEntity<String> updateAuthenticatedUserInfo(
            @RequestBody UpdateAuthenticatedUserInfoRequest request,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        return userService.updateAuthenticatedUserInfo(authenticatedUser, request);
    }

    @DeleteMapping("/user")
    @UserControllerDoc.DeleteAuthenticatedUserDoc
    public ResponseEntity<String> deleteAuthenticatedUser(
            @AuthenticationPrincipal User authenticatedUser
    ) {
        return userService.deleteAuthenticatedUserInfo(authenticatedUser);
    }

    @GetMapping("/users/search/{prefix}")
    @UserControllerDoc.SearchUsersDoc
    public ResponseEntity<SearchUsersResponse> searchUsers(
            @PathVariable String prefix,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        return userService.searchUsers(authenticatedUser, prefix);
    }

    @PostMapping("/users/add/{friendId}")
    @UserControllerDoc.AddFriendDoc
    public ResponseEntity<String> addFriend(
            @PathVariable String friendId,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        return userService.addFriend(authenticatedUser, friendId);
    }

    @DeleteMapping("/users/friends/{friendId}")
    @UserControllerDoc.DeleteFriendDoc
    public ResponseEntity<String> deleteFriend(
            @PathVariable String friendId,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        return userService.deleteFriend(authenticatedUser, friendId);
    }

    @GetMapping("/users/friends/requests/sent")
    @UserControllerDoc.GetSentFriendsRequestsDoc
    public ResponseEntity<GetSentFriendsRequestsResponse> getSentFriendsRequests(
            @AuthenticationPrincipal User authenticatedUser
    ) {
        return userService.getSentFriendsRequests(authenticatedUser);
    }

    @GetMapping("/users/friends/requests/received")
    @UserControllerDoc.GetReceivedFriendsRequestsDoc
    public ResponseEntity<GetReceivedFriendsRequestsResponse> getReceivedFriendsRequests(
            @AuthenticationPrincipal User authenticatedUser
    ) {
        return userService.getReceivedFriendsRequests(authenticatedUser);
    }

    @PutMapping("/users/friends/requests/{requestId}/accept")
    @UserControllerDoc.AcceptFriendRequestDoc
    public ResponseEntity<String> acceptFriendRequest(
            @PathVariable String requestId,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        return userService.acceptFriendRequest(authenticatedUser, requestId);
    }

    @PutMapping("/users/friends/requests/{requestId}/reject")
    @UserControllerDoc.RejectFriendRequestDoc
    public ResponseEntity<String> rejectFriendRequest(
            @PathVariable String requestId,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        return userService.rejectFriendRequest(authenticatedUser, requestId);
    }

    @PutMapping("/users/friends/requests/{requestId}/cancel")
    @UserControllerDoc.CancelFriendRequestDoc
    public ResponseEntity<String> cancelFriendRequest(
            @PathVariable String requestId,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        return userService.cancelFriendRequest(authenticatedUser, requestId);
    }

    @GetMapping("/users/friends/{prefix}")
    @UserControllerDoc.GetFriendsDoc
    public ResponseEntity<GetFriendsResponse> getFriends(
            @PathVariable String prefix,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        return userService.getFriends(authenticatedUser, prefix);
    }

    @PostMapping("/group")
    @UserControllerDoc.CreateGroupDoc
    public ResponseEntity<String> createGroup(
            @RequestBody CreateGroupRequest request,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        return userService.createGroup(authenticatedUser, request);
    }

    @PutMapping("/group/{groupId}")
    @UserControllerDoc.UpdateGroupDoc
    public ResponseEntity<String> updateGroup(
            @PathVariable String groupId,
            @RequestBody UpdateGroupRequest request,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        return userService.updateGroup(authenticatedUser, groupId, request);
    }

    @DeleteMapping("/group/{groupId}")
    @UserControllerDoc.DeleteGroupDoc
    public ResponseEntity<String> deleteGroup(
            @PathVariable String groupId,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        return userService.deleteGroup(authenticatedUser, groupId);
    }

    @GetMapping("/group/{groupId}")
    @UserControllerDoc.GetGroupInfoDoc
    public ResponseEntity<GetGroupInfoResponse> getGroupInfo(
            @PathVariable String groupId,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        return userService.getGroupInfo(authenticatedUser, groupId);
    }

    @PutMapping("/group/{groupId}/members/remove/{userId}")
    @UserControllerDoc.RemoveUserFromGroupDoc
    public ResponseEntity<String> removeUserFromGroup(
            @PathVariable String groupId,
            @PathVariable String userId,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        return userService.removeUserFromGroup(authenticatedUser, groupId, userId);
    }

    @PutMapping("/group/{groupId}/members/add/{userId}")
    @UserControllerDoc.AddUserToGroupDoc
    public ResponseEntity<String> addUserToGroup(
            @PathVariable String groupId,
            @PathVariable String userId,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        return userService.addUserToGroup(authenticatedUser, groupId, userId);
    }

    @GetMapping("/group/{groupId}/invitations")
    @UserControllerDoc.GetGroupInvitationsDoc
    public ResponseEntity<GetGroupInvitationsResponse> getGroupInvitations(
            @PathVariable String groupId,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        return userService.getGroupInvitations(authenticatedUser, groupId);
    }

    @GetMapping("/user/group/invitations")
    @UserControllerDoc.GetUserGroupInvitationsDoc
    public ResponseEntity<GetUserGroupInvitationsResponse> getUserGroupInvitations(
            @AuthenticationPrincipal User authenticatedUser
    ) {
        return userService.getUserGroupInvitations(authenticatedUser);
    }

    @PutMapping("/group/{groupId}/invitation/reject")
    @UserControllerDoc.RejectGroupInvitationDoc
    public ResponseEntity<String> rejectGroupInvitation(
            @PathVariable String groupId,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        return userService.rejectGroupInvitation(authenticatedUser, groupId);
    }

    @PutMapping("/group/{groupId}/invitation/accept")
    @UserControllerDoc.AcceptGroupInvitationDoc
    public ResponseEntity<String> acceptGroupInvitation(
            @PathVariable String groupId,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        return userService.acceptGroupInvitation(authenticatedUser, groupId);
    }

    @PutMapping("/group/{groupId}/invitation/{userId}/cancel")
    @UserControllerDoc.CancelGroupInvitationDoc
    public ResponseEntity<String> cancelGroupInvitation(
            @PathVariable String groupId,
            @PathVariable String userId,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        return userService.cancelGroupInvitation(authenticatedUser, groupId, userId);
    }

}
