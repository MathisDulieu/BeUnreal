package com.supinfo.beunreal_api_gateway.service;

import com.supinfo.beunreal_api_gateway.Producer;
import com.supinfo.beunreal_api_gateway.dao.FriendRequestDao;
import com.supinfo.beunreal_api_gateway.dao.GroupDao;
import com.supinfo.beunreal_api_gateway.dao.UserDao;
import com.supinfo.beunreal_api_gateway.model.common.friendRequest.FriendRequest;
import com.supinfo.beunreal_api_gateway.model.common.friendRequest.FriendRequestStatus;
import com.supinfo.beunreal_api_gateway.model.common.group.Group;
import com.supinfo.beunreal_api_gateway.model.common.group.GroupInvitation;
import com.supinfo.beunreal_api_gateway.model.common.group.GroupInvitationStatus;
import com.supinfo.beunreal_api_gateway.model.common.kafka.KafkaMessage;
import com.supinfo.beunreal_api_gateway.model.common.user.User;
import com.supinfo.beunreal_api_gateway.model.common.user.UserRole;
import com.supinfo.beunreal_api_gateway.model.message.response.model.GroupInfo;
import com.supinfo.beunreal_api_gateway.model.user.request.CreateGroupRequest;
import com.supinfo.beunreal_api_gateway.model.user.request.UpdateAuthenticatedUserInfoRequest;
import com.supinfo.beunreal_api_gateway.model.user.request.UpdateGroupRequest;
import com.supinfo.beunreal_api_gateway.model.user.response.*;
import com.supinfo.beunreal_api_gateway.model.user.response.model.*;
import com.supinfo.beunreal_api_gateway.utils.UserUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;
    private final Producer producer;
    private final UserUtils userUtils;
    private final FriendRequestDao friendRequestDao;
    private final GroupDao groupDao;

    public ResponseEntity<GetUserInfoResponse> getUserInfo(User authenticatedUser, String userId) {
        String targetUserId = (isNull(userId)) ? authenticatedUser.getId() : userId;

        if (!hasPermissionToViewUser(authenticatedUser, targetUserId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        User targetUser = retrieveTargetUser(authenticatedUser, targetUserId);

        if (isNull(targetUser)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        GetUserInfoResponse response = createUserInfoResponse(targetUser);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<String> updateAuthenticatedUserInfo(User authenticatedUser, UpdateAuthenticatedUserInfoRequest request, HttpServletRequest httpRequest) {
        List<String> errors = new ArrayList<>();

        userUtils.validateUpdateAuthenticatedUserDetailsRequest(errors, request);
        if (errors.isEmpty()) {
            userUtils.validateNewUsername(errors, request.getUsername(), authenticatedUser);
            userUtils.validateNewEmail(errors, request.getEmail(), authenticatedUser);
            userUtils.validateNewPassword(errors, request.getOldPassword(), request.getNewPassword(), authenticatedUser);
        }

        if (!errors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userUtils.getErrorsAsString(errors));
        }

        KafkaMessage kafkaMessage = producer.buildKafkaMessage(authenticatedUser, httpRequest, null);

        producer.send(kafkaMessage, "user-events", "updateAuthenticatedUserInfo");

        return ResponseEntity.status(HttpStatus.OK).body("Your profile information has been successfully updated. The changes are now visible in your account.");
    }

    public ResponseEntity<String> deleteAuthenticatedUserInfo(User authenticatedUser, HttpServletRequest httpRequest) {
        KafkaMessage kafkaMessage = producer.buildKafkaMessage(authenticatedUser, httpRequest, null);

        producer.send(kafkaMessage, "user-events", "deleteAuthenticatedUserInfo");

        return ResponseEntity.status(HttpStatus.OK).body("Your account has been successfully deleted.");
    }

    public ResponseEntity<SearchUsersResponse> searchUsers(User authenticatedUser, String prefix) {
        List<User> foundUsers;
        long totalCount;

        if (prefix == null || prefix.trim().isEmpty()) {
            foundUsers = userDao.findAllOrderedByUsernameAsc();
            totalCount = userDao.countAll();
        } else {
            foundUsers = userDao.findByUsernameStartingWith(prefix.trim());
            totalCount = userDao.countByUsernameStartingWith(prefix.trim());
        }

        List<UserToSearch> users = foundUsers.stream()
                .map(user -> UserToSearch.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .profilePicture(user.getProfilePicture())
                        .isFriend(authenticatedUser.getFriendIds().contains(user.getId()))
                        .build())
                .toList();

        SearchUsersResponse response = SearchUsersResponse.builder()
                .users(users)
                .totalUsersFound((int) totalCount)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<String> addFriend(User authenticatedUser, String friendId, HttpServletRequest httpRequest) {
        if (authenticatedUser.getFriendIds().contains(friendId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This user is already in your friends list");
        }

        if (!userDao.exists(friendId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        Map<String, String> kafkaRequest = Map.of(
                "friendId", friendId
        );

        KafkaMessage kafkaMessage = producer.buildKafkaMessage(authenticatedUser, httpRequest, kafkaRequest);

        producer.send(kafkaMessage, "user-events", "addFriend");

        return ResponseEntity.status(HttpStatus.OK).body("Friend request sent successfully");
    }

    public ResponseEntity<String> deleteFriend(User authenticatedUser, String friendId, HttpServletRequest httpRequest) {
        if (!authenticatedUser.getFriendIds().contains(friendId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This user is not in your friends list");
        }

        if (!userDao.exists(friendId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        Map<String, String> kafkaRequest = Map.of(
                "friendId", friendId
        );

        KafkaMessage kafkaMessage = producer.buildKafkaMessage(authenticatedUser, httpRequest, kafkaRequest);

        producer.send(kafkaMessage, "user-events", "deleteFriend");

        return ResponseEntity.status(HttpStatus.OK).body("Friend deleted successfully");
    }

    public ResponseEntity<GetSentFriendsRequestsResponse> getSentFriendsRequests(User authenticatedUser) {
        List<FriendRequest> sentRequests = friendRequestDao.findBySenderId(authenticatedUser.getId());

        if (sentRequests.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    GetSentFriendsRequestsResponse.builder()
                            .friendsRequests(Collections.emptyList())
                            .build()
            );
        }

        List<String> recipientIds = sentRequests.stream()
                .map(FriendRequest::getRecipientId)
                .distinct()
                .collect(Collectors.toList());

        Map<String, User> recipientMap = userDao.findAllByIds(recipientIds).stream()
                .collect(Collectors.toMap(User::getId, user -> user));

        List<SendFriendsRequestsResponse> requestDTOs = sentRequests.stream()
                .map(request -> {
                    User recipient = recipientMap.get(request.getRecipientId());
                    return SendFriendsRequestsResponse.builder()
                            .id(request.getId())
                            .recipient(
                                    UserToSearch.builder()
                                            .id(recipient.getId())
                                            .username(recipient.getUsername())
                                            .profilePicture(recipient.getProfilePicture())
                                            .isFriend(false)
                                            .build()
                            )
                            .status(request.getStatus())
                            .sentAt(request.getCreatedAt())
                            .build();
                })
                .toList();

        GetSentFriendsRequestsResponse response = GetSentFriendsRequestsResponse.builder()
                .friendsRequests(requestDTOs)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<GetReceivedFriendsRequestsResponse> getReceivedFriendsRequests(User authenticatedUser) {
        List<FriendRequest> receivedRequests = friendRequestDao.findByRecipientIdAndStatus(
                authenticatedUser.getId(), FriendRequestStatus.PENDING);

        if (receivedRequests.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    GetReceivedFriendsRequestsResponse.builder()
                            .receivedFriendsRequests(Collections.emptyList())
                            .build()
            );
        }

        List<String> senderIds = receivedRequests.stream()
                .map(FriendRequest::getSenderId)
                .distinct()
                .toList();

        Map<String, User> senderMap = userDao.findAllByIds(senderIds).stream()
                .collect(Collectors.toMap(User::getId, user -> user));

        List<ReceivedFriendsRequestsResponse> requestDTOs = receivedRequests.stream()
                .map(request -> {
                    User sender = senderMap.get(request.getSenderId());
                    return ReceivedFriendsRequestsResponse.builder()
                            .id(request.getId())
                            .sender(UserToSearch.builder()
                                    .id(sender.getId())
                                    .profilePicture(sender.getProfilePicture())
                                    .username(sender.getUsername())
                                    .isFriend(false)
                                    .build())
                            .receivedAt(request.getCreatedAt())
                            .build();
                })
                .collect(Collectors.toList());

        GetReceivedFriendsRequestsResponse response = GetReceivedFriendsRequestsResponse.builder()
                .receivedFriendsRequests(requestDTOs)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<String> acceptFriendRequest(User authenticatedUser, String requestId, HttpServletRequest httpRequest) {
        Optional<FriendRequest> optionalRequest = friendRequestDao.findById(requestId);

        if (optionalRequest.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Friend request not found");
        }

        FriendRequest request = optionalRequest.get();

        if (!request.getRecipientId().equals(authenticatedUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to accept this friend request");
        }

        if (request.getStatus() != FriendRequestStatus.PENDING) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This friend request has already been processed");
        }

        request.setStatus(FriendRequestStatus.ACCEPTED);
        request.setUpdatedAt(LocalDateTime.now());

        Map<String, String> kafkaRequest = Map.of(
                "requestId", requestId
        );

        KafkaMessage kafkaMessage = producer.buildKafkaMessage(authenticatedUser, httpRequest, kafkaRequest);
        producer.send(kafkaMessage, "user-events", "acceptFriendRequest");

        return ResponseEntity.status(HttpStatus.OK).body("Friend request accepted successfully");
    }

    public ResponseEntity<String> rejectFriendRequest(User authenticatedUser, String requestId, HttpServletRequest httpRequest) {
        Optional<FriendRequest> optionalRequest = friendRequestDao.findById(requestId);

        if (optionalRequest.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Friend request not found");
        }

        FriendRequest request = optionalRequest.get();

        if (!request.getRecipientId().equals(authenticatedUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to reject this friend request");
        }

        if (request.getStatus() != FriendRequestStatus.PENDING) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This friend request has already been processed");
        }

        request.setStatus(FriendRequestStatus.REJECTED);
        request.setUpdatedAt(LocalDateTime.now());

        Map<String, String> kafkaRequest = Map.of(
                "requestId", requestId
        );

        KafkaMessage kafkaMessage = producer.buildKafkaMessage(authenticatedUser, httpRequest, kafkaRequest);
        producer.send(kafkaMessage, "user-events", "rejectFriendRequest");

        return ResponseEntity.status(HttpStatus.OK).body("Friend request rejected successfully");
    }

    public ResponseEntity<GetFriendsResponse> getFriends(User authenticatedUser, String prefix) {
        List<String> friendIds = authenticatedUser.getFriendIds();

        if (friendIds == null || friendIds.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    GetFriendsResponse.builder()
                            .friends(Collections.emptyList())
                            .totalFriendsFound(0)
                            .build()
            );
        }

        List<FriendResponse> filteredFriends;

        if (prefix != null && !prefix.trim().isEmpty()) {
            filteredFriends = userDao.findFriendsWithPrefix(friendIds, prefix.trim());
        } else {
            filteredFriends = userDao.findLimitedFriends(friendIds);
        }

        GetFriendsResponse response = GetFriendsResponse.builder()
                .friends(filteredFriends)
                .totalFriendsFound(friendIds.size())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<String> createGroup(User authenticatedUser, CreateGroupRequest request, HttpServletRequest httpRequest) {
        String error = userUtils.validateGroupInfos(request.getName(), request.getGroupPicture());

        if (!isNull(error)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        Map<String, String> kafkaRequest = Map.of(
                "name", request.getName(),
                "picture", request.getGroupPicture()
        );

        KafkaMessage kafkaMessage = producer.buildKafkaMessage(authenticatedUser, httpRequest, kafkaRequest);

        producer.send(kafkaMessage, "user-events", "createGroup");

        return ResponseEntity.status(HttpStatus.OK).body("Group created successfully!");
    }

    public ResponseEntity<String> updateGroup(User authenticatedUser, String groupId, UpdateGroupRequest request, HttpServletRequest httpRequest) {
        Optional<Group> retrievedGroup = groupDao.findGroupById(groupId);
        if (retrievedGroup.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group not found");
        }

        if (!retrievedGroup.get().getAdminIds().contains(authenticatedUser.getId())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not authorized to update this group");
        }

        String error = userUtils.validateNewGroupInfos(request.getName(), request.getGroupPicture());

        if (!isNull(error)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        Map<String, String> kafkaRequest = Map.of(
                "groupId", groupId,
                "name", request.getName(),
                "picture", request.getGroupPicture()
        );

        KafkaMessage kafkaMessage = producer.buildKafkaMessage(authenticatedUser, httpRequest, kafkaRequest);

        producer.send(kafkaMessage, "user-events", "updateGroup");

        return ResponseEntity.status(HttpStatus.OK).body("Group updated successfully!");
    }

    public ResponseEntity<String> deleteGroup(User authenticatedUser, String groupId, HttpServletRequest httpRequest) {
        Optional<Group> retrievedGroup = groupDao.findGroupById(groupId);
        if (retrievedGroup.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group not found");
        }

        if (!retrievedGroup.get().getCreatorId().equals(authenticatedUser.getId())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not authorized to delete this group");
        }

        Map<String, String> kafkaRequest = Map.of(
                "groupId", groupId
        );

        KafkaMessage kafkaMessage = producer.buildKafkaMessage(authenticatedUser, httpRequest, kafkaRequest);

        producer.send(kafkaMessage, "user-events", "deleteGroup");

        return ResponseEntity.status(HttpStatus.OK).body("Group deleted successfully!");
    }

    public ResponseEntity<GetGroupInfoResponse> getGroupInfo(User authenticatedUser, String groupId) {
        Optional<Group> optionalRetrievedGroup = groupDao.findGroupById(groupId);
        if (optionalRetrievedGroup.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (!optionalRetrievedGroup.get().getMemberIds().contains(authenticatedUser.getId())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Group retrievedGroup = optionalRetrievedGroup.get();

        List<User> admins = userDao.findAllByIds(retrievedGroup.getAdminIds());
        List<User> members = userDao.findAllByIds(retrievedGroup.getMemberIds());
        Optional<User> creator = userDao.findById(retrievedGroup.getCreatorId());

        if (creator.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        List<FriendResponse> adminResponse = admins.stream()
                .map(admin -> FriendResponse.builder()
                        .id(admin.getId())
                        .username(admin.getUsername())
                        .profilePicture(admin.getProfilePicture())
                        .status(admin.getStatus())
                        .build())
                .collect(Collectors.toList());

        List<FriendResponse> membersResponse = members.stream()
                .map(member -> FriendResponse.builder()
                        .id(member.getId())
                        .username(member.getUsername())
                        .profilePicture(member.getProfilePicture())
                        .status(member.getStatus())
                        .build())
                .collect(Collectors.toList());

        FriendResponse creatorResponse = FriendResponse.builder()
                .id(creator.get().getId())
                .profilePicture(creator.get().getProfilePicture())
                .status(creator.get().getStatus())
                .username(creator.get().getUsername())
                .build();

        GetGroupInfoResponse response = GetGroupInfoResponse.builder()
                .id(retrievedGroup.getId())
                .name(retrievedGroup.getName())
                .groupPicture(retrievedGroup.getGroupPicture())
                .createdAt(retrievedGroup.getCreatedAt())
                .admins(adminResponse)
                .members(membersResponse)
                .creator(creatorResponse)
                .updatedAt(retrievedGroup.getUpdatedAt())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<String> removeUserFromGroup(User authenticatedUser, String groupId, String userId, HttpServletRequest httpRequest) {
        Optional<Group> retrievedGroup = groupDao.findGroupById(groupId);
        if (retrievedGroup.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group not found");
        }

        if (!retrievedGroup.get().getAdminIds().contains(authenticatedUser.getId())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not authorized to remove a user from this group");
        }

        Optional<User> retrievedUser = userDao.findById(userId);
        if (retrievedUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        if (!retrievedGroup.get().getCreatorId().equals(authenticatedUser.getId())) {
            if (retrievedGroup.get().getAdminIds().contains(retrievedUser.get().getId())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not authorized to remove this user from this group");
            }
        }

        Map<String, String> kafkaRequest = Map.of(
                "groupId", groupId,
                "userId", userId
        );

        KafkaMessage kafkaMessage = producer.buildKafkaMessage(authenticatedUser, httpRequest, kafkaRequest);

        producer.send(kafkaMessage, "user-events", "removeUserFromGroup");

        return ResponseEntity.status(HttpStatus.OK).body("User removed successfully from the group!");
    }

    public ResponseEntity<String> addUserToGroup(User authenticatedUser, String groupId, String userId, HttpServletRequest httpRequest) {
        Optional<Group> retrievedGroup = groupDao.findGroupById(groupId);
        if (retrievedGroup.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group not found");
        }

        if (!retrievedGroup.get().getAdminIds().contains(authenticatedUser.getId())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not authorized to add a user to this group");
        }

        Optional<User> retrievedUser = userDao.findById(userId);
        if (retrievedUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        if (retrievedGroup.get().getMemberIds().contains(userId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User is already in the group");
        }

        Map<String, String> kafkaRequest = Map.of(
                "groupId", groupId,
                "userId", userId
        );

        KafkaMessage kafkaMessage = producer.buildKafkaMessage(authenticatedUser, httpRequest, kafkaRequest);

        producer.send(kafkaMessage, "user-events", "addUserToGroup");

        return ResponseEntity.status(HttpStatus.OK).body("Group invitation has been sent to " + retrievedUser.get().getUsername());
    }

    public ResponseEntity<GetGroupInvitationsResponse> getGroupInvitations(User authenticatedUser, String groupId) {
        Optional<Group> retrievedGroup = groupDao.findGroupById(groupId);
        if (retrievedGroup.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (!retrievedGroup.get().getMemberIds().contains(authenticatedUser.getId())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<GroupInvitation> groupInvitations = groupDao.findAllInvitationsByGroupId(groupId);

        if (groupInvitations.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    GetGroupInvitationsResponse.builder()
                            .groupInvitations(Collections.emptyList())
                            .build()
            );
        }

        Set<String> userIds = new HashSet<>();
        groupInvitations.forEach(invitation -> {
            userIds.add(invitation.getSenderId());
            userIds.add(invitation.getReceiverId());
        });

        List<User> users = userDao.findAllByIds(new ArrayList<>(userIds));
        Map<String, User> userMap = users.stream()
                .collect(Collectors.toMap(User::getId, user -> user));

        List<GroupInvitationsResponse> groupInvitationsResponses = groupInvitations.stream()
                .map(invitation -> {
                    User sender = userMap.getOrDefault(invitation.getSenderId(), null);
                    User receiver = userMap.getOrDefault(invitation.getReceiverId(), null);

                    if (isNull(sender) || isNull(receiver)) {
                        return null;
                    }

                    UserToSearch senderResponse = UserToSearch.builder()
                            .id(sender.getId())
                            .username(sender.getUsername())
                            .profilePicture(sender.getProfilePicture())
                            .build();

                    UserToSearch receiverResponse = UserToSearch.builder()
                            .id(receiver.getId())
                            .username(receiver.getUsername())
                            .profilePicture(receiver.getProfilePicture())
                            .build();

                    return GroupInvitationsResponse.builder()
                            .sender(senderResponse)
                            .receiver(receiverResponse)
                            .status(invitation.getStatus())
                            .sentDate(invitation.getSentDate())
                            .build();
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        GetGroupInvitationsResponse response = GetGroupInvitationsResponse.builder()
                .groupInvitations(groupInvitationsResponses)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<GetUserGroupInvitationsResponse> getUserGroupInvitations(User authenticatedUser) {
        List<GroupInvitation> userInvitations = groupDao.findByReceiverIdAndStatus(
                authenticatedUser.getId(), GroupInvitationStatus.PENDING);

        if (userInvitations.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    GetUserGroupInvitationsResponse.builder()
                            .userGroupInvitations(Collections.emptyList())
                            .build()
            );
        }

        Set<String> senderIds = userInvitations.stream()
                .map(GroupInvitation::getSenderId)
                .collect(Collectors.toSet());

        Set<String> groupIds = userInvitations.stream()
                .map(GroupInvitation::getGroupId)
                .collect(Collectors.toSet());

        List<User> senders = userDao.findAllByIds(new ArrayList<>(senderIds));
        Map<String, User> senderMap = senders.stream()
                .collect(Collectors.toMap(User::getId, user -> user));

        List<Group> groups = groupDao.findAllByIds(new ArrayList<>(groupIds));
        Map<String, Group> groupMap = groups.stream()
                .collect(Collectors.toMap(Group::getId, group -> group));

        List<UserGroupInvitationsResponse> invitationResponses = userInvitations.stream()
                .map(invitation -> {
                    User sender = senderMap.getOrDefault(invitation.getSenderId(), null);
                    Group group = groupMap.getOrDefault(invitation.getGroupId(), null);

                    if (isNull(sender) || isNull(group)) {
                        return null;
                    }

                    UserToSearch senderResponse = UserToSearch.builder()
                            .id(sender.getId())
                            .username(sender.getUsername())
                            .profilePicture(sender.getProfilePicture())
                            .build();

                    GroupInfo groupResponse = GroupInfo.builder()
                            .id(group.getId())
                            .name(group.getName())
                            .groupPicture(group.getGroupPicture())
                            .build();

                    return UserGroupInvitationsResponse.builder()
                            .sender(senderResponse)
                            .group(groupResponse)
                            .sentDate(invitation.getSentDate())
                            .build();
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        GetUserGroupInvitationsResponse response = GetUserGroupInvitationsResponse.builder()
                .userGroupInvitations(invitationResponses)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<String> rejectGroupInvitation(User authenticatedUser, String invitationId, HttpServletRequest httpRequest) {
        Optional<GroupInvitation> optionalInvitation = groupDao.findGroupInvitationById(invitationId);
        if (optionalInvitation.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group invitation not found");
        }

        GroupInvitation invitation = optionalInvitation.get();

        if (!invitation.getReceiverId().equals(authenticatedUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to reject this invitation");
        }

        if (invitation.getStatus() != GroupInvitationStatus.PENDING) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This invitation has already been processed");
        }

        Map<String, String> kafkaRequest = Map.of(
                "invitationId", invitationId
        );

        KafkaMessage kafkaMessage = producer.buildKafkaMessage(authenticatedUser, httpRequest, kafkaRequest);
        producer.send(kafkaMessage, "user-events", "rejectGroupInvitation");

        return ResponseEntity.status(HttpStatus.OK).body("Group invitation rejected successfully");
    }

    public ResponseEntity<String> acceptGroupInvitation(User authenticatedUser, String invitationId, HttpServletRequest httpRequest) {
        Optional<GroupInvitation> optionalInvitation = groupDao.findGroupInvitationById(invitationId);
        if (optionalInvitation.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group invitation not found");
        }

        GroupInvitation invitation = optionalInvitation.get();

        if (!invitation.getReceiverId().equals(authenticatedUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to accept this invitation");
        }

        if (invitation.getStatus() != GroupInvitationStatus.PENDING) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This invitation has already been processed");
        }

        Optional<Group> optionalGroup = groupDao.findGroupById(invitation.getGroupId());
        if (optionalGroup.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The group no longer exists");
        }

        Group group = optionalGroup.get();
        if (group.getMemberIds().contains(authenticatedUser.getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You are already a member of this group");
        }

        Map<String, String> kafkaRequest = Map.of(
                "invitationId", invitationId
        );

        KafkaMessage kafkaMessage = producer.buildKafkaMessage(authenticatedUser, httpRequest, kafkaRequest);
        producer.send(kafkaMessage, "user-events", "acceptGroupInvitation");

        return ResponseEntity.status(HttpStatus.OK).body("Group invitation accepted successfully");
    }

    public ResponseEntity<String> cancelGroupInvitation(User authenticatedUser, String groupId, String invitationId, HttpServletRequest httpRequest) {
        Optional<Group> optionalGroup = groupDao.findGroupById(groupId);
        if (optionalGroup.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group not found");
        }

        Group group = optionalGroup.get();

        if (!group.getAdminIds().contains(authenticatedUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to cancel invitations for this group");
        }

        Optional<GroupInvitation> optionalInvitation = groupDao.findGroupInvitationById(invitationId);
        if (optionalInvitation.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invitation not found");
        }

        GroupInvitation invitation = optionalInvitation.get();

        if (!invitation.getGroupId().equals(groupId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This invitation does not belong to the specified group");
        }

        if (invitation.getStatus() != GroupInvitationStatus.PENDING) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This invitation has already been processed");
        }

        Map<String, String> kafkaRequest = Map.of(
                "invitationId", invitationId
        );

        KafkaMessage kafkaMessage = producer.buildKafkaMessage(authenticatedUser, httpRequest, kafkaRequest);
        producer.send(kafkaMessage, "user-events", "cancelGroupInvitation");

        return ResponseEntity.status(HttpStatus.OK).body("Group invitation canceled successfully");
    }

    public ResponseEntity<String> cancelFriendRequest(User authenticatedUser, String requestId, HttpServletRequest httpRequest) {
        Optional<FriendRequest> optionalFriendRequest = friendRequestDao.findById(requestId);
        if (optionalFriendRequest.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Friend request not found");
        }

        FriendRequest friendRequest = optionalFriendRequest.get();

        if (!friendRequest.getSenderId().equals(authenticatedUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to cancel this friend request");
        }

        if (friendRequest.getStatus() != FriendRequestStatus.PENDING) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This friend request has already been processed");
        }

        Map<String, String> kafkaRequest = Map.of(
                "requestId", requestId
        );

        KafkaMessage kafkaMessage = producer.buildKafkaMessage(authenticatedUser, httpRequest, kafkaRequest);
        producer.send(kafkaMessage, "user-events", "cancelFriendRequest");

        return ResponseEntity.status(HttpStatus.OK).body("Friend request canceled successfully");
    }

    public ResponseEntity<GetUserGroupsResponse> getUserGroups(User authenticatedUser) {
        List<Group> userGroups = groupDao.findGroupsByMemberId(authenticatedUser.getId());

        if (userGroups.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    GetUserGroupsResponse.builder()
                            .groups(Collections.emptyList())
                            .build()
            );
        }

        List<GroupsResponse> groups = userGroups.stream()
                .map(group -> GroupsResponse.builder()
                        .id(group.getId())
                        .name(group.getName())
                        .groupPicture(group.getGroupPicture())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(
                GetUserGroupsResponse.builder()
                        .groups(groups)
                        .build()
        );
    }

    private boolean hasPermissionToViewUser(User authenticatedUser, String userId) {
        return authenticatedUser.getRole().equals(UserRole.ADMIN)
                || Objects.equals(userId, authenticatedUser.getId());
    }

    private User retrieveTargetUser(User authenticatedUser, String userId) {
        if (authenticatedUser.getId().equals(userId)) {
            return authenticatedUser;
        } else {
            return userDao.findById(userId).orElse(null);
        }
    }

    private GetUserInfoResponse createUserInfoResponse(User user) {
        return GetUserInfoResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .username(user.getUsername())
                .profilePicture(user.getProfilePicture())
                .build();
    }

}
