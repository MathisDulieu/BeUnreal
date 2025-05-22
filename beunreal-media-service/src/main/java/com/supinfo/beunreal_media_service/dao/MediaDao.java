package com.supinfo.beunreal_media_service.dao;

import com.supinfo.beunreal_media_service.model.Media;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MediaDao {

    private final MongoTemplate mongoTemplate;

    private static final String MEDIA_COLLECTION = "media";

    public Optional<Media> findById(String mediaId) {
        return Optional.ofNullable(mongoTemplate.findById(mediaId, Media.class, MEDIA_COLLECTION));
    }

    public void save(Media media) {
        mongoTemplate.save(media, MEDIA_COLLECTION);
    }

}
