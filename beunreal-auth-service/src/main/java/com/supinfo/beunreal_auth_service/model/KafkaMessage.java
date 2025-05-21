package com.supinfo.beunreal_auth_service.model;

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