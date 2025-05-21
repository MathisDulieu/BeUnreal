package com.supinfo.beunreal_message_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
    private List<String> friendIds = new ArrayList<>();

    @Builder.Default
    private Set<String> pendingFriendRequestIds = new HashSet<>();

}
