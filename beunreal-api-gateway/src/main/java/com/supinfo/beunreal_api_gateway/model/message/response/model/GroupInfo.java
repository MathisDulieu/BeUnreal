package com.supinfo.beunreal_api_gateway.model.message.response.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class GroupInfo {

    private String id;
    private String name;
    private String groupPicture;

}
