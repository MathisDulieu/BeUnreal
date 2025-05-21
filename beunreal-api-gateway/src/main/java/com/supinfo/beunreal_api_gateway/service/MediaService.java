package com.supinfo.beunreal_api_gateway.service;

import com.supinfo.beunreal_api_gateway.Producer;
import com.supinfo.beunreal_api_gateway.model.common.kafka.KafkaMessage;
import com.supinfo.beunreal_api_gateway.model.common.user.User;
import com.supinfo.beunreal_api_gateway.utils.ImageUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MediaService {

    private final ImageUtils imageUtils;
    private final Producer producer;

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
