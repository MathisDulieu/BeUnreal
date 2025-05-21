package com.supinfo.beunreal_api_gateway.model.common.group;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private List<String> memberIds = new ArrayList<>();

    @Builder.Default
    private List<String> adminIds = new ArrayList<>();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}