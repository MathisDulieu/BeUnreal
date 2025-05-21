package com.supinfo.beunreal_user_service.service;

import com.supinfo.beunreal_user_service.UuidProvider;
import com.supinfo.beunreal_user_service.dao.GroupDao;
import com.supinfo.beunreal_user_service.dao.UserDao;
import com.supinfo.beunreal_user_service.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Objects.isNull;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupDao groupDao;
    private final UuidProvider uuidProvider;
    private final UserDao userDao;

    public void createGroup(KafkaMessage kafkaMessage) {
        Map<String, String> request = kafkaMessage.getRequest();
        String name = request.get("name");
        String picture = request.get("picture");

        Group group = Group.builder()
                .id(uuidProvider.generateUuid())
                .groupPicture(picture)
                .creatorId(kafkaMessage.getAuthenticatedUser().getId())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .name(name)
                .build();

        List<String> adminIds = group.getAdminIds();
        adminIds.add(kafkaMessage.getAuthenticatedUser().getId());
        group.setAdminIds(adminIds);

        List<String> memberIds = group.getMemberIds();
        memberIds.add(kafkaMessage.getAuthenticatedUser().getId());
        group.setMemberIds(memberIds);

        groupDao.saveGroup(group);

        log.info("Group with name : " + name + " has been created!");
    }

    public void updateGroup(KafkaMessage kafkaMessage) {
        Map<String, String> request = kafkaMessage.getRequest();
        String name = request.get("name");
        String picture = request.get("picture");
        String groupId = request.get("groupId");

        Optional<Group> group = groupDao.findGroupById(groupId);
        if (group.isEmpty()) {
            log.warn("Group with id : " + groupId + " does not exist");
        } else {
            if (!isNull(name)) {
                group.get().setName(name);
            }

            if (!isNull(picture)) {
                group.get().setGroupPicture(picture);
            }

            groupDao.saveGroup(group.get());

            log.info("Group with id : " + groupId + " updated successfully!");
        }
    }

    public void deleteGroup(KafkaMessage kafkaMessage) {
        Map<String, String> request = kafkaMessage.getRequest();
        String groupId = request.get("groupId");

        Optional<Group> group = groupDao.findGroupById(groupId);
        if (group.isEmpty()) {
            log.warn("Group with id : " + groupId + " does not exist");
        } else {
            groupDao.deleteGroup(group.get());
            groupDao.deleteGroupInvitationsByGroupId(groupId);
        }
    }

    public void removeUserFromGroup(KafkaMessage kafkaMessage) {
        Map<String, String> request = kafkaMessage.getRequest();
        String groupId = request.get("groupId");
        String userId = request.get("userId");

        Optional<Group> group = groupDao.findGroupById(groupId);
        Optional<User> user = userDao.findById(userId);

        if (group.isEmpty()) {
            log.warn("Group with id : " + groupId + " does not exist");
            return;
        }

        if (user.isEmpty()) {
            log.warn("User with id : " + userId + " does not exist");
            return;
        }

        List<String> memberIds = group.get().getMemberIds();
        memberIds.remove(userId);
        group.get().setMemberIds(memberIds);

        groupDao.saveGroup(group.get());
        log.info("User with id : " + userId + " has been removed from group with id : " + groupId);
    }

    public void addUserToGroup(KafkaMessage kafkaMessage) {
        Map<String, String> request = kafkaMessage.getRequest();
        String groupId = request.get("groupId");
        String userId = request.get("userId");

        GroupInvitation groupInvitation = GroupInvitation.builder()
                .groupId(groupId)
                .senderId(kafkaMessage.getAuthenticatedUser().getId())
                .status(GroupInvitationStatus.PENDING)
                .receiverId(userId)
                .sentDate(LocalDateTime.now())
                .build();

        groupDao.saveGroupInvitation(groupInvitation);

        log.info("User with id : " + userId + " has been added to group with id : " + groupId);
    }

    public void rejectGroupInvitation(KafkaMessage kafkaMessage) {
        Map<String, String> request = kafkaMessage.getRequest();
        String invitationId = request.get("invitationId");

        Optional<GroupInvitation> groupInvitation = groupDao.findGroupInvitationById(invitationId);

        if (groupInvitation.isEmpty()) {
            log.warn("Group Invitation with id : " + invitationId + " does not exist");
            return;
        }

        groupInvitation.get().setStatus(GroupInvitationStatus.REJECTED);

        groupDao.saveGroupInvitation(groupInvitation.get());

        log.info("Group invitation with id : " + invitationId + " has been rejected");
    }

    public void acceptGroupInvitation(KafkaMessage kafkaMessage) {
        Map<String, String> request = kafkaMessage.getRequest();
        String invitationId = request.get("invitationId");

        Optional<GroupInvitation> groupInvitation = groupDao.findGroupInvitationById(invitationId);

        if (groupInvitation.isEmpty()) {
            log.warn("Group Invitation with id : " + invitationId + " does not exist");
            return;
        }

        groupInvitation.get().setStatus(GroupInvitationStatus.ACCEPTED);

        groupDao.saveGroupInvitation(groupInvitation.get());

        Optional<Group> group = groupDao.findGroupById(groupInvitation.get().getGroupId());

        if (group.isEmpty()) {
            log.warn("Group with id : " + groupInvitation.get().getGroupId() + " does not exist");
            return;
        }

        List<String> memberIds = group.get().getMemberIds();
        memberIds.add(kafkaMessage.getAuthenticatedUser().getId());
        group.get().setMemberIds(memberIds);

        groupDao.saveGroup(group.get());

        log.info("Group invitation with id : " + invitationId + " has been accepted");
    }

    public void cancelGroupInvitation(KafkaMessage kafkaMessage) {
        Map<String, String> request = kafkaMessage.getRequest();
        String invitationId = request.get("invitationId");

        Optional<GroupInvitation> groupInvitation = groupDao.findGroupInvitationById(invitationId);

        if (groupInvitation.isEmpty()) {
            log.warn("Group Invitation with id : " + invitationId + " does not exist");
            return;
        }

        groupDao.deleteGroupInvitation(groupInvitation.get());

        log.info("Group invitation with id : " + invitationId + " has been canceled");
    }

}
