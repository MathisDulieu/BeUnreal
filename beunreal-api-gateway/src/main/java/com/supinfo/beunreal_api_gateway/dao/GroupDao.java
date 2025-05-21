package com.supinfo.beunreal_api_gateway.dao;

import com.supinfo.beunreal_api_gateway.model.common.group.Group;
import com.supinfo.beunreal_api_gateway.model.common.group.GroupInvitation;
import com.supinfo.beunreal_api_gateway.model.common.group.GroupInvitationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GroupDao {

    private final MongoTemplate mongoTemplate;

    private static final String GROUP_COLLECTION = "groups";
    private static final String GROUP_INVITATION_COLLECTION = "groups_invitations";

    public Optional<Group> findGroupById(String groupId) {
        return Optional.ofNullable(mongoTemplate.findById(groupId, Group.class, GROUP_COLLECTION));
    }

    public Optional<GroupInvitation> findGroupInvitationById(String groupInvitationId) {
        return Optional.ofNullable(mongoTemplate.findById(groupInvitationId, GroupInvitation.class, GROUP_INVITATION_COLLECTION));
    }

    public List<GroupInvitation> findAllInvitationsByGroupId(String groupId) {
        Query query = new Query(Criteria.where("groupId").is(groupId));
        return mongoTemplate.find(query, GroupInvitation.class, GROUP_INVITATION_COLLECTION);
    }

    public List<GroupInvitation> findByReceiverIdAndStatus(String receiverId, GroupInvitationStatus status) {
        Query query = new Query(Criteria.where("receiverId").is(receiverId)
                .and("status").is(status));
        return mongoTemplate.find(query, GroupInvitation.class, GROUP_INVITATION_COLLECTION);
    }

    public List<Group> findAllByIds(List<String> groupIds) {
        if (groupIds == null || groupIds.isEmpty()) {
            return Collections.emptyList();
        }

        Query query = new Query(Criteria.where("_id").in(groupIds));
        return mongoTemplate.find(query, Group.class, GROUP_COLLECTION);
    }



}
