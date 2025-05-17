package com.supinfo.beunreal_api_gateway.model.common.group;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "groups_invitations")
public class GroupInvitation {
    private String senderId;
    private String receiverId;
    private String groupId;
    private LocalDateTime sentDate;
    private GroupInvitationStatus status;
}
