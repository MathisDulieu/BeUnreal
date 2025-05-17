package com.supinfo.beunreal_api_gateway.model.common.story;

import com.supinfo.beunreal_api_gateway.model.common.Location;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "stories")
public class Story {

    @Id
    private String id;
    private String userId;
    private String mediaId;
    private Location location;
    private String caption;
    private LocalDateTime createdAt;

}
