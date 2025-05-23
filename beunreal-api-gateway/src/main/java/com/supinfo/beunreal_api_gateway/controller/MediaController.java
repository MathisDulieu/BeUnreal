package com.supinfo.beunreal_api_gateway.controller;

import com.supinfo.beunreal_api_gateway.model.common.user.User;
import com.supinfo.beunreal_api_gateway.model.discovery.request.PostMediaRequest;
import com.supinfo.beunreal_api_gateway.model.discovery.response.GetNearbyDiscoveriesResponse;
import com.supinfo.beunreal_api_gateway.model.media.response.GetMediaResponse;
import com.supinfo.beunreal_api_gateway.service.MediaService;
import com.supinfo.beunreal_api_gateway.swagger.MediaControllerDoc;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class MediaController {

    private final MediaService mediaService;

    @PostMapping("/private/media/{mediaId}/share/{groupId}")
    @MediaControllerDoc.ShareMediaToGroupDoc
    public ResponseEntity<String> shareMediaToGroup(
            @PathVariable String mediaId,
            @PathVariable String groupId,
            @AuthenticationPrincipal User authenticatedUser,
            HttpServletRequest httpRequest
    ) {
        return mediaService.shareMediaToGroup(authenticatedUser, mediaId, groupId, httpRequest);
    }

    @PostMapping("/private/media/{mediaId}/share/{userId}")
    @MediaControllerDoc.ShareMediaToUserDoc
    public ResponseEntity<String> shareMediaToUser(
            @PathVariable String mediaId,
            @PathVariable String userId,
            @AuthenticationPrincipal User authenticatedUser,
            HttpServletRequest httpRequest
    ) {
        return mediaService.shareMediaToUser(authenticatedUser, mediaId, userId, httpRequest);
    }

    @PostMapping("/private/messages/media")
    @MediaControllerDoc.GetMediaUrlDoc
    public ResponseEntity<String> getMediaUrl(
            @RequestParam("file") MultipartFile file
    ) {
        return mediaService.getMediaUrl(file);
    }

    @GetMapping("/private/media/users/nearby?lat={latitude}&lon={longitude}")
    @MediaControllerDoc.GetNearbyDiscoveriesDoc
    public ResponseEntity<GetNearbyDiscoveriesResponse> getNearbyDiscoveries(
            @PathVariable String latitude,
            @PathVariable String longitude
    ) {
        return mediaService.getNearbyDiscoveries(latitude, longitude);
    }

    @PostMapping("/private/media/stories")
    @MediaControllerDoc.PostStoryDoc
    public ResponseEntity<String> postStory(
            @RequestBody PostMediaRequest request,
            @AuthenticationPrincipal User authenticatedUser,
            HttpServletRequest httpRequest
    ) {
        return mediaService.postMedia(authenticatedUser, request, httpRequest);
    }

    @GetMapping("/private/media/stories/{mediaId}")
    @MediaControllerDoc.GetMediaDoc
    public ResponseEntity<GetMediaResponse> getMedia(
            @PathVariable String mediaId
    ) {
        return mediaService.getMedia(mediaId);
    }

}
