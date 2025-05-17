package com.supinfo.beunreal_api_gateway.model.media.request;

import com.supinfo.beunreal_api_gateway.model.user.response.model.UserToSearch;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShareMediaToUserRequest {
    private String mediaUrl;
    private UserToSearch creator;
    private LocalDateTime createdAt;
}
