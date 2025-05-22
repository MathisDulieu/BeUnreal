package com.supinfo.beunreal_api_gateway.dao;

import com.supinfo.beunreal_api_gateway.model.common.media.Media;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MediaDao {

    private final MongoTemplate mongoTemplate;

    private static final String MEDIA_COLLECTION = "media";

    public Optional<Media> findById(String mediaId) {
        return Optional.ofNullable(mongoTemplate.findById(mediaId, Media.class, MEDIA_COLLECTION));
    }

    public List<Media> findNearbyMedia(double latitude, double longitude) {
        double radiusInDegrees = 0.09;

        Query query = new Query();

        Criteria locationCriteria = new Criteria().andOperator(
                Criteria.where("location.latitude").gte(latitude - radiusInDegrees).lte(latitude + radiusInDegrees),
                Criteria.where("location.longitude").gte(longitude - radiusInDegrees).lte(longitude + radiusInDegrees)
        );

        Criteria notExpiredCriteria = Criteria.where("expiresAt").gt(LocalDateTime.now());

        query.addCriteria(locationCriteria);
        query.addCriteria(notExpiredCriteria);

        query.with(Sort.by(Sort.Direction.DESC, "createdAt"));

        query.limit(50);

        return mongoTemplate.find(query, Media.class, MEDIA_COLLECTION);
    }

}