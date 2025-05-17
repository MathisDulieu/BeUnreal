package com.supinfo.beunreal_api_gateway.model.message.response;

import com.supinfo.beunreal_api_gateway.model.message.response.model.GroupInfo;
import com.supinfo.beunreal_api_gateway.model.message.response.model.GroupMessage;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class GetGroupConversationResponse {
    private GroupInfo group;
    private List<GroupMessage> messages;
}
