package com.supinfo.beunreal_api_gateway.model.user.response;

import com.supinfo.beunreal_api_gateway.model.common.friendRequest.FriendRequestStatus;
import com.supinfo.beunreal_api_gateway.model.user.response.model.ReceivedFriendsRequestsResponse;
import com.supinfo.beunreal_api_gateway.model.user.response.model.UserToSearch;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class GetReceivedFriendsRequestsResponse {

    private List<ReceivedFriendsRequestsResponse> receivedFriendsRequests;

}
