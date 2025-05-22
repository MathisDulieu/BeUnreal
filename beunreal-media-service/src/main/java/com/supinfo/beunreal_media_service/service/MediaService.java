package com.supinfo.beunreal_media_service.service;

import com.supinfo.beunreal_media_service.UuidProvider;
import com.supinfo.beunreal_media_service.dao.MediaDao;
import com.supinfo.beunreal_media_service.dao.MessageDao;
import com.supinfo.beunreal_media_service.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.Uuid;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MediaService {

    private final UuidProvider uuidProvider;
    private final MessageDao messageDao;
    private final MediaDao mediaDao;

    public void shareMediaToGroup(KafkaMessage kafkaMessage) {
        Map<String, String> request = kafkaMessage.getRequest();
        String groupId = request.get("groupId");
        String mediaId = request.get("mediaId");

        Optional<Media> optionalMedia = mediaDao.findById(mediaId);
        if (optionalMedia.isEmpty()) {
            log.warn("Media not found with id : " + mediaId);
            return;
        }

        Media media = optionalMedia.get();

        Message message = Message.builder()
                .id(uuidProvider.generateUuid())
                .mediaUrl(media.getUrl())
                .deliveredAt(LocalDateTime.now())
                .sentAt(LocalDateTime.now())
                .status(MessageStatus.DELIVERED)
                .senderId(kafkaMessage.getAuthenticatedUser().getId())
                .groupId(groupId)
                .build();

        messageDao.save(message);

        log.info("Media with id : " + mediaId + " has been shared with group : " + groupId);
    }

    public void shareMediaToUser(KafkaMessage kafkaMessage) {
        Map<String, String> request = kafkaMessage.getRequest();
        String userId = request.get("userId");
        String mediaId = request.get("mediaId");

        Optional<Media> optionalMedia = mediaDao.findById(mediaId);
        if (optionalMedia.isEmpty()) {
            log.warn("Media not found with id : " + mediaId);
            return;
        }

        Media media = optionalMedia.get();

        Message message = Message.builder()
                .id(uuidProvider.generateUuid())
                .mediaUrl(media.getUrl())
                .deliveredAt(LocalDateTime.now())
                .sentAt(LocalDateTime.now())
                .status(MessageStatus.DELIVERED)
                .senderId(kafkaMessage.getAuthenticatedUser().getId())
                .recipientId(userId)
                .build();

        messageDao.save(message);

        log.info("Media with id : " + mediaId + " has been shared with user : " + userId);
    }

    public void postMedia(KafkaMessage kafkaMessage) {
        Map<String, String> request = kafkaMessage.getRequest();
        String mediaUrl = request.get("mediaUrl");
        String latitude = request.get("latitude");
        String longitude = request.get("longitude");

        double lat = Double.parseDouble(latitude);
        double lng = Double.parseDouble(longitude);

        MediaType mediaType = determineMediaType(mediaUrl);

        Media media = Media.builder()
                .id(uuidProvider.generateUuid())
                .url(mediaUrl)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusHours(24))
                .location(Location.builder()
                        .latitude(lat)
                        .longitude(lng)
                        .build())
                .ownerId(kafkaMessage.getAuthenticatedUser().getId())
                .type(mediaType)
                .build();

        mediaDao.save(media);

        log.info("Media posted successfully with type: {}", mediaType);
    }

    private MediaType determineMediaType(String mediaUrl) {
        if (mediaUrl == null || mediaUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("Media URL cannot be null or empty");
        }

        String lowercaseUrl = mediaUrl.toLowerCase();

        if (lowercaseUrl.endsWith(".jpg") ||
                lowercaseUrl.endsWith(".jpeg") ||
                lowercaseUrl.endsWith(".png") ||
                lowercaseUrl.endsWith(".gif") ||
                lowercaseUrl.endsWith(".webp") ||
                lowercaseUrl.endsWith(".svg") ||
                lowercaseUrl.endsWith(".bmp") ||
                lowercaseUrl.endsWith(".tiff")) {
            return MediaType.IMAGE;
        }

        if (lowercaseUrl.endsWith(".mp4") ||
                lowercaseUrl.endsWith(".avi") ||
                lowercaseUrl.endsWith(".mov") ||
                lowercaseUrl.endsWith(".wmv") ||
                lowercaseUrl.endsWith(".webm") ||
                lowercaseUrl.endsWith(".mkv") ||
                lowercaseUrl.endsWith(".flv") ||
                lowercaseUrl.endsWith(".m4v")) {
            return MediaType.VIDEO;
        }

        log.warn("Unknown media extension for URL: {}. Defaulting to IMAGE type.", mediaUrl);
        return MediaType.IMAGE;
    }

}
