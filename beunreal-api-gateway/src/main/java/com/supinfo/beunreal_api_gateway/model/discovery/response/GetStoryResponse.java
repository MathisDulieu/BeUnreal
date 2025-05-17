package com.supinfo.beunreal_api_gateway.model.discovery.response;

import com.supinfo.beunreal_api_gateway.model.common.Location;
import com.supinfo.beunreal_api_gateway.model.user.response.model.UserToSearch;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class GetStoryResponse {

    private String id;
    private UserToSearch creator;
    private String mediaUrl;
    private Location location;
    private String caption;
    private LocalDateTime createdAt;

}
