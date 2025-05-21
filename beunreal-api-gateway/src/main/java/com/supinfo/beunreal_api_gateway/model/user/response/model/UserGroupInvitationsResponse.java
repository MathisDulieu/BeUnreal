package com.supinfo.beunreal_api_gateway.model.user.response.model;

import com.supinfo.beunreal_api_gateway.model.message.response.model.GroupInfo;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserGroupInvitationsResponse {
    private UserToSearch sender;
    private LocalDateTime sentDate;
    private String groupName;
    private GroupInfo group;
}
