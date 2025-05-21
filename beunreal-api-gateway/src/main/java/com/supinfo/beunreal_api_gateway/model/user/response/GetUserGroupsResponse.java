package com.supinfo.beunreal_api_gateway.model.user.response;

import com.supinfo.beunreal_api_gateway.model.user.response.model.GroupsResponse;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class GetUserGroupsResponse {
    private List<GroupsResponse> groups;
}
