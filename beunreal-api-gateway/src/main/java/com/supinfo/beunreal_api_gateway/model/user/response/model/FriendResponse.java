package com.supinfo.beunreal_api_gateway.model.user.response.model;

import com.supinfo.beunreal_api_gateway.model.common.user.UserStatus;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class FriendResponse {

    private String id;
    private String username;
    private String profilePicture;
    private UserStatus status;

}
