package com.supinfo.beunreal_api_gateway.model.user.response.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class GroupsResponse {
    private String id;
    private String name;
    private String groupPicture;
}
