package com.supinfo.beunreal_api_gateway.model.user.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAuthenticatedUserInfoRequest {

    private String username;
    private String email;
    private String oldPassword;
    private String newPassword;
    private String profilePicture;

}
