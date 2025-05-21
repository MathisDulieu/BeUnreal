package com.supinfo.beunreal_api_gateway.model.user.response.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserToSearch {

    private String id;
    private String username;
    private String profilePicture;
    private boolean isFriend;

}
