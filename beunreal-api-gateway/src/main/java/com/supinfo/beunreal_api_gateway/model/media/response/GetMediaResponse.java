package com.supinfo.beunreal_api_gateway.model.media.response;

import com.supinfo.beunreal_api_gateway.model.user.response.model.UserToSearch;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class GetMediaResponse {
    private String mediaUrl;
    private LocalDateTime createdAt;
    private UserToSearch creator;
}
