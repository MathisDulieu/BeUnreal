package com.supinfo.beunreal_api_gateway.service;

import com.supinfo.beunreal_api_gateway.model.common.user.User;
import com.supinfo.beunreal_api_gateway.model.discovery.request.PostStoryRequest;
import com.supinfo.beunreal_api_gateway.model.discovery.response.GetNearbyDiscoveriesResponse;
import com.supinfo.beunreal_api_gateway.model.discovery.response.GetStoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DiscoveryService {
    public ResponseEntity<GetNearbyDiscoveriesResponse> getNearbyDiscoveries(User authenticatedUser, String latitude, String longitude) {
        return null;
    }

    public ResponseEntity<String> postStory(User authenticatedUser, PostStoryRequest request) {
        return null;
    }

    public ResponseEntity<GetStoryResponse> getStory(User authenticatedUser, String storyId) {
        return null;
    }

    public ResponseEntity<String> deleteStory(User authenticatedUser, String storyId) {
        return null;
    }
}
