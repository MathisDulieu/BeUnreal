package com.supinfo.beunreal_api_gateway.service;

import com.supinfo.beunreal_api_gateway.model.common.user.User;
import com.supinfo.beunreal_api_gateway.model.media.request.ShareMediaToGroupRequest;
import com.supinfo.beunreal_api_gateway.model.media.request.ShareMediaToUserRequest;
import com.supinfo.beunreal_api_gateway.model.media.request.UploadMediaRequest;
import com.supinfo.beunreal_api_gateway.model.media.response.GetMediaResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MediaService {

    public ResponseEntity<String> uploadMedia(User authenticatedUser, UploadMediaRequest request) {
        return null;
    }

    public ResponseEntity<GetMediaResponse> getMedia(User authenticatedUser, String mediaId) {
        return null;
    }

    public ResponseEntity<String> deleteMedia(User authenticatedUser, String mediaId) {
        return null;
    }

    public ResponseEntity<String> shareMediaToGroup(User authenticatedUser, ShareMediaToGroupRequest request, String mediaId, String groupId) {
        return null;
    }

    public ResponseEntity<String> shareMediaToUser(User authenticatedUser, ShareMediaToUserRequest request, String mediaId, String userId) {
        return null;
    }

    public ResponseEntity<String> getMediaUrl(User authenticatedUser, MultipartFile file) {
        return null;
    }

}
