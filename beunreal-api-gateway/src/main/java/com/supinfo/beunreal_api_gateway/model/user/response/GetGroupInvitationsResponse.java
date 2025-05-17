package com.supinfo.beunreal_api_gateway.model.user.response;

import com.supinfo.beunreal_api_gateway.model.common.group.GroupInvitationStatus;
import com.supinfo.beunreal_api_gateway.model.user.response.model.UserToSearch;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class GetGroupInvitationsResponse {

    private UserToSearch sender;
    private UserToSearch receiver;
    private LocalDateTime sentDate;
    private GroupInvitationStatus status;

}
