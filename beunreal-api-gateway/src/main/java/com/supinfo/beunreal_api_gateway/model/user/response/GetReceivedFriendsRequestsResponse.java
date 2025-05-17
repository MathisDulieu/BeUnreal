package com.supinfo.beunreal_api_gateway.model.user.response;

import com.supinfo.beunreal_api_gateway.model.common.friendRequest.FriendRequestStatus;
import com.supinfo.beunreal_api_gateway.model.user.response.model.UserToSearch;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class GetReceivedFriendsRequestsResponse {

    private String id;
    private UserToSearch sender;
    private FriendRequestStatus status;
    private LocalDateTime receivedAt;

}
