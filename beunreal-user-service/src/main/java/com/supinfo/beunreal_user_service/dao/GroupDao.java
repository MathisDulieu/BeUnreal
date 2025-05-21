package com.supinfo.beunreal_user_service.dao;

import com.supinfo.beunreal_user_service.model.Group;
import com.supinfo.beunreal_user_service.model.GroupInvitation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GroupDao {

    private final MongoTemplate mongoTemplate;

    private static final String GROUP_COLLECTION = "groups";
    private static final String GROUP_INVITATION_COLLECTION = "groups_invitations";

    public void saveGroup(Group group) {
        mongoTemplate.save(group, GROUP_COLLECTION);
    }

    public void saveGroupInvitation(GroupInvitation groupInvitation) {
        mongoTemplate.save(groupInvitation, GROUP_INVITATION_COLLECTION);
    }

    public Optional<Group> findGroupById(String groupId) {
        return Optional.ofNullable(mongoTemplate.findById(groupId, Group.class, GROUP_COLLECTION));
    }

    public void deleteGroup(Group group) {
        mongoTemplate.remove(group, GROUP_COLLECTION);
    }

    public void deleteGroupInvitation(GroupInvitation groupInvitation) {
        mongoTemplate.remove(groupInvitation, GROUP_INVITATION_COLLECTION);
    }

    public void deleteGroupInvitationsByGroupId(String groupId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("groupId").is(groupId));
        mongoTemplate.remove(query, GroupInvitation.class, GROUP_INVITATION_COLLECTION);
    }

    public Optional<GroupInvitation> findGroupInvitationById(String groupInvitationId) {
        return Optional.ofNullable(mongoTemplate.findById(groupInvitationId, GroupInvitation.class, GROUP_INVITATION_COLLECTION));
    }
}
