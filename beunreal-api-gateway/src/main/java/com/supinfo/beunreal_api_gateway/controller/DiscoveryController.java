package com.supinfo.beunreal_api_gateway.controller;

import com.supinfo.beunreal_api_gateway.model.common.user.User;
import com.supinfo.beunreal_api_gateway.model.discovery.request.PostStoryRequest;
import com.supinfo.beunreal_api_gateway.model.discovery.response.GetNearbyDiscoveriesResponse;
import com.supinfo.beunreal_api_gateway.model.discovery.response.GetStoryResponse;
import com.supinfo.beunreal_api_gateway.service.DiscoveryService;
import com.supinfo.beunreal_api_gateway.swagger.DiscoveryControllerDoc;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class DiscoveryController {

    private final DiscoveryService discoveryService;

    @GetMapping("/discovery/users/nearby?lat={latitude}&lon={longitude}")
    @DiscoveryControllerDoc.GetNearbyDiscoveriesDoc
    public ResponseEntity<GetNearbyDiscoveriesResponse> getNearbyDiscoveries(
            @PathVariable String latitude,
            @PathVariable String longitude,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        return discoveryService.getNearbyDiscoveries(authenticatedUser, latitude, longitude);
    }

    @PostMapping("/discovery/stories")
    @DiscoveryControllerDoc.PostStoryDoc
    public ResponseEntity<String> postStory(
            @RequestBody PostStoryRequest request,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        return discoveryService.postStory(authenticatedUser, request);
    }

    @GetMapping("/discovery/stories/{storyId}")
    @DiscoveryControllerDoc.GetStoryDoc
    public ResponseEntity<GetStoryResponse> getStory(
            @PathVariable String storyId,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        return discoveryService.getStory(authenticatedUser, storyId);
    }

    @DeleteMapping("/discovery/stories/{storyId}")
    @DiscoveryControllerDoc.DeleteStoryDoc
    public ResponseEntity<String> deleteStory(
            @PathVariable String storyId,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        return discoveryService.deleteStory(authenticatedUser, storyId);
    }

}
