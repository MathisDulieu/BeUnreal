package com.supinfo.beunreal_api_gateway.model.common.group;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "groups")
public class Group {

    @Id
    private String id;
    private String name;
    private String creatorId;
    private String groupPicture;

    @Builder.Default
    private Set<String> memberIds = new HashSet<>();

    @Builder.Default
    private Set<String> adminIds = new HashSet<>();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}