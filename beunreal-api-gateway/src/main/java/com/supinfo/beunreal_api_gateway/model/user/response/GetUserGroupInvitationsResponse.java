package com.supinfo.beunreal_api_gateway.model.user.response;

import com.supinfo.beunreal_api_gateway.model.user.response.model.UserToSearch;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class GetUserGroupInvitationsResponse {

    private UserToSearch sender;
    private LocalDateTime sentDate;
    private String groupName;
    private String groupId;

}
