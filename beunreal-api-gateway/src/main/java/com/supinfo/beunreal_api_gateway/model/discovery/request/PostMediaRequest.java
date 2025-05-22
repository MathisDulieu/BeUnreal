package com.supinfo.beunreal_api_gateway.model.discovery.request;

import com.supinfo.beunreal_api_gateway.model.common.Location;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostMediaRequest {

    private String mediaUrl;
    private Location location;

}
