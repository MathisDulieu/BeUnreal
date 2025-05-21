package com.supinfo.beunreal_api_gateway.model.common.kafka;

import com.supinfo.beunreal_api_gateway.model.common.user.User;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class KafkaMessage {
    private Map<String, String> request;
    private String ipAddress;
    private User authenticatedUser;
    private String timeStamp;
}