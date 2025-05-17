package com.supinfo.beunreal_api_gateway.model.user.response;

import com.supinfo.beunreal_api_gateway.model.common.user.UserRole;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class GetAuthenticatedUserInfoResponse {

    private String id;
    private String username;
    private String email;
    private String profilePicture;
    private UserRole role;

}
