package com.supinfo.beunreal_api_gateway.model.discovery.response;

import com.supinfo.beunreal_api_gateway.model.discovery.response.model.MediaResponse;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class GetNearbyDiscoveriesResponse {

    private List<MediaResponse> stories;

}
