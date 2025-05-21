package com.supinfo.beunreal_api_gateway.model.user.response;

import com.supinfo.beunreal_api_gateway.model.user.response.model.FriendResponse;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class GetGroupInfoResponse {

    private String id;
    private String name;
    private FriendResponse creator;
    private String groupPicture;
    private List<FriendResponse> members;
    private List<FriendResponse> admins;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
