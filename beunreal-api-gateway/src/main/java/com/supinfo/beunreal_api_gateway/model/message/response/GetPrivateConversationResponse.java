package com.supinfo.beunreal_api_gateway.model.message.response;

import com.supinfo.beunreal_api_gateway.model.message.response.model.PrivateMessage;
import com.supinfo.beunreal_api_gateway.model.user.response.model.FriendResponse;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class GetPrivateConversationResponse {
    private FriendResponse friend;
    private List<PrivateMessage> messages;
}
