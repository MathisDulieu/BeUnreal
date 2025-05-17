package com.supinfo.beunreal_api_gateway.model.message.response.model;

import com.supinfo.beunreal_api_gateway.model.common.message.MessageStatus;
import com.supinfo.beunreal_api_gateway.model.user.response.model.FriendResponse;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class GroupMessage {

    private String id;
    private FriendResponse sender;
    private String content;
    private String mediaUrl;
    private LocalDateTime sentAt;
    private MessageStatus status;

}
