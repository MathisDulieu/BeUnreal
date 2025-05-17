package com.supinfo.beunreal_api_gateway.model.common.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User {

    @Id
    private String id;

    @Indexed(unique = true)
    private String username;

    @Indexed(unique = true)
    private String email;

    private String password;
    private String profilePicture;
    private UserStatus status;

    @Builder.Default
    private UserRole role = UserRole.USER;

    @Builder.Default
    private Set<String> friendIds = new HashSet<>();

    @Builder.Default
    private Set<String> pendingFriendRequestIds = new HashSet<>();

}

