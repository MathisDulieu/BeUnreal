package com.supinfo.beunreal_api_gateway.service;

import com.supinfo.beunreal_api_gateway.model.common.user.User;
import com.supinfo.beunreal_api_gateway.model.user.request.CreateGroupRequest;
import com.supinfo.beunreal_api_gateway.model.user.request.UpdateAuthenticatedUserInfoRequest;
import com.supinfo.beunreal_api_gateway.model.user.request.UpdateGroupRequest;
import com.supinfo.beunreal_api_gateway.model.user.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    public ResponseEntity<GetAuthenticatedUserInfoResponse> getAuthenticatedUserInfo(User authenticatedUser, String userId) {
        return null;
    }

    public ResponseEntity<String> updateAuthenticatedUserInfo(User authenticatedUser, UpdateAuthenticatedUserInfoRequest request) {
        return null;
    }

    public ResponseEntity<String> deleteAuthenticatedUserInfo(User authenticatedUser) {
        return null;
    }

    public ResponseEntity<SearchUsersResponse> searchUsers(User authenticatedUser, String prefix) {
        return null;
    }

    public ResponseEntity<String> addFriend(User authenticatedUser, String friendId) {
        return null;
    }

    public ResponseEntity<String> deleteFriend(User authenticatedUser, String friendId) {
        return null;
    }

    public ResponseEntity<GetSentFriendsRequestsResponse> getSentFriendsRequests(User authenticatedUser) {
        return null;
    }

    public ResponseEntity<GetReceivedFriendsRequestsResponse> getReceivedFriendsRequests(User authenticatedUser) {
        return null;
    }

    public ResponseEntity<String> acceptFriendRequest(User authenticatedUser, String requestId) {
        return null;
    }

    public ResponseEntity<String> rejectFriendRequest(User authenticatedUser, String requestId) {
        return null;
    }

    public ResponseEntity<GetFriendsResponse> getFriends(User authenticatedUser, String prefix) {
        return null;
    }

    public ResponseEntity<String> createGroup(User authenticatedUser, CreateGroupRequest request) {
        return null;
    }

    public ResponseEntity<String> updateGroup(User authenticatedUser, String groupId, UpdateGroupRequest request) {
        return null;
    }

    public ResponseEntity<String> deleteGroup(User authenticatedUser, String groupId) {
        return null;
    }

    public ResponseEntity<GetGroupInfoResponse> getGroupInfo(User authenticatedUser, String groupId) {
        return null;
    }

    public ResponseEntity<String> removeUserFromGroup(User authenticatedUser, String groupId, String userId) {
        return null;
    }

    public ResponseEntity<String> addUserToGroup(User authenticatedUser, String groupId, String userId) {
        return null;
    }

    public ResponseEntity<GetGroupInvitationsResponse> getGroupInvitations(User authenticatedUser, String groupId) {
        return null;
    }

    public ResponseEntity<GetUserGroupInvitationsResponse> getUserGroupInvitations(User authenticatedUser) {
        return null;
    }

    public ResponseEntity<String> rejectGroupInvitation(User authenticatedUser, String groupId) {
        return null;
    }

    public ResponseEntity<String> acceptGroupInvitation(User authenticatedUser, String groupId) {
        return null;
    }

    public ResponseEntity<String> cancelGroupInvitation(User authenticatedUser, String groupId, String userId) {
        return null;
    }

    public ResponseEntity<String> updateUserLocation(User authenticatedUser, UpdateUserLocationRequest request) {
        return null;
    }

    public ResponseEntity<String> cancelFriendRequest(User authenticatedUser, String requestId) {
        return null;
    }
}
