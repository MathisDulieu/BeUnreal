package com.supinfo.beunreal_api_gateway.dao;

import com.supinfo.beunreal_api_gateway.model.common.group.Group;
import com.supinfo.beunreal_api_gateway.model.common.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GroupDao {

    private final MongoTemplate mongoTemplate;

    private static final String GROUP_COLLECTION = "groups";

    public Optional<Group> findById(String groupId) {
        return Optional.ofNullable(mongoTemplate.findById(groupId, Group.class, GROUP_COLLECTION));
    }

}
