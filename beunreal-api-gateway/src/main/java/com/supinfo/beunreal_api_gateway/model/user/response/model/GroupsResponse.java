package com.supinfo.beunreal_api_gateway.model.user.response.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

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
