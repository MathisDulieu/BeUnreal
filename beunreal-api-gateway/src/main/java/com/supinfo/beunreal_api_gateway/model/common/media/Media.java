package com.supinfo.beunreal_api_gateway.model.common.media;

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
@Document(collection = "media")
public class Media {

    @Id
    private String id;
    private String ownerId;
    private MediaType type;
    private String url;
    private Location location;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;

}