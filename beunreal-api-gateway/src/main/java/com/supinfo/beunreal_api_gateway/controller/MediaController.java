package com.supinfo.beunreal_api_gateway.controller;

import com.supinfo.beunreal_api_gateway.model.common.user.User;
import com.supinfo.beunreal_api_gateway.model.media.request.ShareMediaToGroupRequest;
import com.supinfo.beunreal_api_gateway.model.media.request.ShareMediaToUserRequest;
import com.supinfo.beunreal_api_gateway.model.media.request.UploadMediaRequest;
import com.supinfo.beunreal_api_gateway.model.media.response.GetMediaResponse;
import com.supinfo.beunreal_api_gateway.service.MediaService;
import com.supinfo.beunreal_api_gateway.swagger.MediaControllerDoc;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class MediaController {

    private final MediaService mediaService;

    @PostMapping("/media/upload")
    @MediaControllerDoc.UploadMediaDoc
    public ResponseEntity<String> uploadMedia(
            @RequestBody UploadMediaRequest request,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        return mediaService.uploadMedia(authenticatedUser, request);
    }

    @GetMapping("/media/{mediaId}")
    @MediaControllerDoc.GetMediaDoc
    public ResponseEntity<GetMediaResponse> getMedia(
            @PathVariable String mediaId,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        return mediaService.getMedia(authenticatedUser, mediaId);
    }

    @DeleteMapping("/media/{mediaId}")
    @MediaControllerDoc.DeleteMediaDoc
    public ResponseEntity<String> deleteMedia(
            @PathVariable String mediaId,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        return mediaService.deleteMedia(authenticatedUser, mediaId);
    }

    @PostMapping("/media/{mediaId}/share/{groupId}")
    @MediaControllerDoc.ShareMediaToGroupDoc
    public ResponseEntity<String> shareMediaToGroup(
            @RequestBody ShareMediaToGroupRequest request,
            @PathVariable String mediaId,
            @PathVariable String groupId,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        return mediaService.shareMediaToGroup(authenticatedUser, request, mediaId, groupId);
    }

    @PostMapping("/media/{mediaId}/share/{userId}")
    @MediaControllerDoc.ShareMediaToUserDoc
    public ResponseEntity<String> shareMediaToUser(
            @RequestBody ShareMediaToUserRequest request,
            @PathVariable String mediaId,
            @PathVariable String userId,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        return mediaService.shareMediaToUser(authenticatedUser, request, mediaId, userId);
    }

    @PostMapping("/messages/media")
    @MediaControllerDoc.GetMediaUrlDoc
    public ResponseEntity<String> getMediaUrl(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        return mediaService.getMediaUrl(authenticatedUser, file);
    }

}
