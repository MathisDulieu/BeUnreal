package com.supinfo.beunreal_api_gateway.service;

import com.supinfo.beunreal_api_gateway.Producer;
import com.supinfo.beunreal_api_gateway.dao.MediaDao;
import com.supinfo.beunreal_api_gateway.dao.UserDao;
import com.supinfo.beunreal_api_gateway.model.common.kafka.KafkaMessage;
import com.supinfo.beunreal_api_gateway.model.common.media.Media;
import com.supinfo.beunreal_api_gateway.model.common.user.User;
import com.supinfo.beunreal_api_gateway.model.discovery.request.PostMediaRequest;
import com.supinfo.beunreal_api_gateway.model.discovery.response.GetNearbyDiscoveriesResponse;
import com.supinfo.beunreal_api_gateway.model.discovery.response.model.MediaResponse;
import com.supinfo.beunreal_api_gateway.model.media.response.GetMediaResponse;
import com.supinfo.beunreal_api_gateway.model.user.response.model.UserToSearch;
import com.supinfo.beunreal_api_gateway.utils.ImageUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MediaService {

    private final ImageUtils imageUtils;
    private final Producer producer;
    private final MediaDao mediaDao;
    private final UserDao userDao;

    public ResponseEntity<String> shareMediaToGroup(User authenticatedUser, String mediaId, String groupId, HttpServletRequest httpRequest) {
        Map<String, String> kafkaRequest = Map.of(
                "groupId", groupId,
                "mediaId", mediaId
        );

        KafkaMessage kafkaMessage = producer.buildKafkaMessage(authenticatedUser, httpRequest, kafkaRequest);

        producer.send(kafkaMessage, "media-events", "shareMediaToGroup");

        return ResponseEntity.status(HttpStatus.OK).body("Media shared successfully");
    }

    public ResponseEntity<String> shareMediaToUser(User authenticatedUser, String mediaId, String userId, HttpServletRequest httpRequest) {
        Map<String, String> kafkaRequest = Map.of(
                "userId", userId,
                "mediaId", mediaId
        );

        KafkaMessage kafkaMessage = producer.buildKafkaMessage(authenticatedUser, httpRequest, kafkaRequest);

        producer.send(kafkaMessage, "media-events", "shareMediaToUser");

        return ResponseEntity.status(HttpStatus.OK).body("Media shared successfully");
    }

    public ResponseEntity<String> getMediaUrl(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No file provided");
        }

        String validationError = validateMediaFile(file);
        if (validationError != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationError);
        }

        try {
            String mediaUrl = imageUtils.uploadMedia(file);

            return ResponseEntity.ok(mediaUrl);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload media. Please try again later.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred. Please try again later.");
        }
    }

    public ResponseEntity<GetNearbyDiscoveriesResponse> getNearbyDiscoveries(String latitude, String longitude) {
        if (latitude == null || longitude == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        try {
            double lat = Double.parseDouble(latitude);
            double lng = Double.parseDouble(longitude);

            if (lat < -90 || lat > 90 || lng < -180 || lng > 180) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            List<Media> nearbyMedia = mediaDao.findNearbyMedia(lat, lng);

            List<MediaResponse> discoveries = new ArrayList<>();

            for (Media media : nearbyMedia) {
                Optional<User> optionalUser = userDao.findById(media.getOwnerId());
                if (optionalUser.isEmpty()) {
                    continue;
                }

                User creator = optionalUser.get();

                UserToSearch creatorInfo = UserToSearch.builder()
                        .id(creator.getId())
                        .username(creator.getUsername())
                        .profilePicture(creator.getProfilePicture())
                        .build();

                MediaResponse discovery = MediaResponse.builder()
                        .id(media.getId())
                        .mediaUrl(media.getUrl())
                        .creator(creatorInfo)
                        .location(media.getLocation())
                        .createdAt(media.getCreatedAt())
                        .build();

                discoveries.add(discovery);
            }

            GetNearbyDiscoveriesResponse response = GetNearbyDiscoveriesResponse.builder()
                    .stories(discoveries)
                    .build();

            return ResponseEntity.ok(response);

        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    public ResponseEntity<String> postMedia(User authenticatedUser, PostMediaRequest request, HttpServletRequest httpRequest) {
        Map<String, String> kafkaRequest = Map.of(
                "mediaUrl", request.getMediaUrl(),
                "latitude", String.valueOf(request.getLocation().getLatitude()),
                "longitude", String.valueOf(request.getLocation().getLongitude())
        );

        KafkaMessage kafkaMessage = producer.buildKafkaMessage(authenticatedUser, httpRequest, kafkaRequest);

        producer.send(kafkaMessage, "media-events", "postMedia");

        return ResponseEntity.status(HttpStatus.OK).body("Media posted successfully");
    }

    public ResponseEntity<GetMediaResponse> getMedia(String mediaId) {
        Optional<Media> optionalMedia = mediaDao.findById(mediaId);
        if (optionalMedia.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Media media = optionalMedia.get();

        Optional<User> optionalUser = userDao.findById(media.getOwnerId());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        User user = optionalUser.get();

        UserToSearch creator = UserToSearch.builder()
                .id(user.getId())
                .profilePicture(user.getProfilePicture())
                .username(user.getUsername())
                .build();

        GetMediaResponse response = GetMediaResponse.builder()
                .mediaUrl(media.getUrl())
                .createdAt(media.getCreatedAt())
                .creator(creator)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    private String validateMediaFile(MultipartFile file) {
        long maxFileSize = 50 * 1024 * 1024;
        if (file.getSize() > maxFileSize) {
            return "File size exceeds maximum limit of 50MB";
        }

        String contentType = file.getContentType();

        boolean isValidImage = contentType.startsWith("image/") &&
                (contentType.equals("image/jpeg") ||
                        contentType.equals("image/png") ||
                        contentType.equals("image/gif") ||
                        contentType.equals("image/webp"));

        boolean isValidVideo = contentType.startsWith("video/") &&
                (contentType.equals("video/mp4") ||
                        contentType.equals("video/avi") ||
                        contentType.equals("video/mov") ||
                        contentType.equals("video/wmv") ||
                        contentType.equals("video/webm"));

        if (!isValidImage && !isValidVideo) {
            return "File type not supported. Please upload an image (JPEG, PNG, GIF, WebP) or video (MP4, AVI, MOV, WMV, WebM)";
        }

        return null;
    }

}
