package com.supinfo.beunreal_api_gateway.model.message.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SendPrivateMessageRequest {
    private String userId;
    private String content;
}
