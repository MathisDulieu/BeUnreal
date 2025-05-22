package com.supinfo.beunreal_api_gateway.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class UserControllerDoc {

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            tags = {"User"},
            summary = "Get user information",
            description = "Retrieves detailed information about a user by their ID. " +
                    "Use 'me' as userId to get information about the authenticated user. " +
                    "Only admins can view other users' full information.",
            security = @SecurityRequirement(name = "bearerAuth"),
            parameters = {
                    @Parameter(
                            name = "userId",
                            description = "User ID or 'me' for authenticated user",
                            required = true,
                            examples = {
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(name = "My Profile", value = "me"),
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(name = "Other User", value = "64a1b2c3d4e5f6789abcdef0")
                            }
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User information retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = com.supinfo.beunreal_api_gateway.model.user.response.GetUserInfoResponse.class),
                            examples = @ExampleObject(
                                    name = "User Profile",
                                    value = """
                                            {
                                                "id": "64a1b2c3d4e5f6789abcdef0",
                                                "username": "johndoe",
                                                "email": "john.doe@example.com",
                                                "profilePicture": "https://example.com/avatar.jpg",
                                                "role": "USER"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Authentication required",
                    content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "Authentication required"))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - Insufficient permissions",
                    content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "Access denied"))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found - User not found",
                    content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "User not found"))
            )
    })
    public @interface GetUserInfoDoc {}

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            tags = {"User"},
            summary = "Update authenticated user information",
            description = "Updates the authenticated user's profile information. " +
                    "At least one field must be provided. Password update requires the current password for verification.",
            security = @SecurityRequirement(name = "bearerAuth"),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User information to update",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = com.supinfo.beunreal_api_gateway.model.user.request.UpdateAuthenticatedUserInfoRequest.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Update Username",
                                            summary = "Update only username",
                                            value = """
                                                    {
                                                        "username": "newusername"
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Update Email",
                                            summary = "Update only email",
                                            value = """
                                                    {
                                                        "email": "newemail@example.com"
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Update Password",
                                            summary = "Update password with verification",
                                            value = """
                                                    {
                                                        "oldPassword": "currentPassword123!",
                                                        "newPassword": "newSecurePass456@"
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Complete Update",
                                            summary = "Update multiple fields",
                                            value = """
                                                    {
                                                        "username": "newusername",
                                                        "email": "newemail@example.com",
                                                        "profilePicture": "https://example.com/new-avatar.jpg",
                                                        "oldPassword": "currentPassword123!",
                                                        "newPassword": "newSecurePass456@"
                                                    }
                                                    """
                                    )
                            }
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Profile updated successfully",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Your profile information has been successfully updated. The changes are now visible in your account.")
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request - Validation errors",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = {
                                    @ExampleObject(
                                            name = "No Fields",
                                            summary = "No fields provided",
                                            value = "At least one field (email, username, or password) must be provided to update your profile. Please specify what you want to change."
                                    ),
                                    @ExampleObject(
                                            name = "Invalid Username",
                                            summary = "Username validation failed",
                                            value = "Invalid username format. Username must be between 3-20 characters and contain only letters, numbers, and underscores."
                                    ),
                                    @ExampleObject(
                                            name = "Username Taken",
                                            summary = "Username already exists",
                                            value = "This username is already taken. Please choose a different username."
                                    ),
                                    @ExampleObject(
                                            name = "Invalid Email",
                                            summary = "Email validation failed",
                                            value = "Invalid email format. Please enter a valid email address."
                                    ),
                                    @ExampleObject(
                                            name = "Email Taken",
                                            summary = "Email already registered",
                                            value = "This email address is already registered with another account. Please use a different email address."
                                    ),
                                    @ExampleObject(
                                            name = "Weak Password",
                                            summary = "Password requirements not met",
                                            value = "Password does not meet security requirements. Please use at least 8 characters including uppercase, lowercase, numbers, and special characters."
                                    ),
                                    @ExampleObject(
                                            name = "Wrong Current Password",
                                            summary = "Current password incorrect",
                                            value = "Incorrect current password. Please enter your current password correctly to verify your identity."
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Authentication required",
                    content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "Authentication required"))
            )
    })
    public @interface UpdateAuthenticatedUserInfoDoc {}

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            tags = {"User"},
            summary = "Delete authenticated user account",
            description = "Permanently deletes the authenticated user's account and all associated data. " +
                    "This action cannot be undone.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Account deleted successfully",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Your account has been successfully deleted.")
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Authentication required",
                    content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "Authentication required"))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error - Failed to delete account",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Failed to delete account. Please try again later.")
                    )
            )
    })
    public @interface DeleteAuthenticatedUserDoc {}

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            tags = {"User"},
            summary = "Search users",
            description = "Search for users by username prefix. If no prefix is provided, returns all users " +
                    "ordered by username. Results include friendship status with the authenticated user.",
            security = @SecurityRequirement(name = "bearerAuth"),
            parameters = {
                    @Parameter(
                            name = "prefix",
                            description = "Username prefix to search for (optional)",
                            required = false,
                            examples = {
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(name = "Search by prefix", value = "john"),
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(name = "No prefix", value = "")
                            }
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Search completed successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = com.supinfo.beunreal_api_gateway.model.user.response.SearchUsersResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Search Results",
                                            summary = "Users found matching criteria",
                                            value = """
                                                    {
                                                        "totalUsersFound": 2,
                                                        "users": [
                                                            {
                                                                "id": "64a1b2c3d4e5f6789abcdef0",
                                                                "username": "johndoe",
                                                                "profilePicture": "https://example.com/avatar1.jpg",
                                                                "isFriend": true
                                                            },
                                                            {
                                                                "id": "64a1b2c3d4e5f6789abcdef1",
                                                                "username": "johnsmith",
                                                                "profilePicture": "https://example.com/avatar2.jpg",
                                                                "isFriend": false
                                                            }
                                                        ]
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "No Results",
                                            summary = "No users found",
                                            value = """
                                                    {
                                                        "totalUsersFound": 0,
                                                        "users": []
                                                    }
                                                    """
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Authentication required",
                    content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "Authentication required"))
            )
    })
    public @interface SearchUsersDoc {}


    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            tags = {"User"},
            summary = "Send friend request",
            description = "Sends a friend request to the specified user. " +
                    "Cannot send request if already friends or if user doesn't exist.",
            security = @SecurityRequirement(name = "bearerAuth"),
            parameters = {
                    @Parameter(
                            name = "friendId",
                            description = "ID of the user to send friend request to",
                            required = true,
                            example = "64a1b2c3d4e5f6789abcdef0"
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Friend request sent successfully",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Friend request sent successfully")
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request - Already friends",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "This user is already in your friends list")
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Authentication required",
                    content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "Authentication required"))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found - User not found",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "User not found")
                    )
            )
    })
    public @interface AddFriendDoc {}

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            tags = {"User"},
            summary = "Remove friend",
            description = "Removes a user from the authenticated user's friends list.",
            security = @SecurityRequirement(name = "bearerAuth"),
            parameters = {
                    @Parameter(
                            name = "friendId",
                            description = "ID of the friend to remove",
                            required = true,
                            example = "64a1b2c3d4e5f6789abcdef0"
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Friend removed successfully",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Friend deleted successfully")
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request - Not friends",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "This user is not in your friends list")
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Authentication required",
                    content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "Authentication required"))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found - User not found",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "User not found")
                    )
            )
    })
    public @interface DeleteFriendDoc {}

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            tags = {"User"},
            summary = "Get sent friend requests",
            description = "Retrieves all friend requests sent by the authenticated user with their current status.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Sent friend requests retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = com.supinfo.beunreal_api_gateway.model.user.response.GetSentFriendsRequestsResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "With Requests",
                                            summary = "User has sent friend requests",
                                            value = """
                                                    {
                                                        "friendsRequests": [
                                                            {
                                                                "id": "64a1b2c3d4e5f6789abcdef0",
                                                                "recipient": {
                                                                    "id": "64a1b2c3d4e5f6789abcdef1",
                                                                    "username": "johndoe",
                                                                    "profilePicture": "https://example.com/avatar.jpg",
                                                                    "isFriend": false
                                                                },
                                                                "status": "PENDING",
                                                                "sentAt": "2024-01-15T10:30:00"
                                                            }
                                                        ]
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "No Requests",
                                            summary = "No sent requests",
                                            value = """
                                                    {
                                                        "friendsRequests": []
                                                    }
                                                    """
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Authentication required",
                    content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "Authentication required"))
            )
    })
    public @interface GetSentFriendsRequestsDoc {}

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            tags = {"User"},
            summary = "Get received friend requests",
            description = "Retrieves all pending friend requests received by the authenticated user.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Received friend requests retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = com.supinfo.beunreal_api_gateway.model.user.response.GetReceivedFriendsRequestsResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "With Requests",
                                            summary = "User has pending requests",
                                            value = """
                                                    {
                                                        "receivedFriendsRequests": [
                                                            {
                                                                "id": "64a1b2c3d4e5f6789abcdef0",
                                                                "sender": {
                                                                    "id": "64a1b2c3d4e5f6789abcdef1",
                                                                    "username": "johndoe",
                                                                    "profilePicture": "https://example.com/avatar.jpg",
                                                                    "isFriend": false
                                                                },
                                                                "status": "PENDING",
                                                                "receivedAt": "2024-01-15T10:30:00"
                                                            }
                                                        ]
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "No Requests",
                                            summary = "No pending requests",
                                            value = """
                                                    {
                                                        "receivedFriendsRequests": []
                                                    }
                                                    """
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Authentication required",
                    content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "Authentication required"))
            )
    })
    public @interface GetReceivedFriendsRequestsDoc {}

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            tags = {"User"},
            summary = "Accept friend request",
            description = "Accepts a pending friend request. Only the recipient can accept the request.",
            security = @SecurityRequirement(name = "bearerAuth"),
            parameters = {
                    @Parameter(
                            name = "requestId",
                            description = "ID of the friend request to accept",
                            required = true,
                            example = "64a1b2c3d4e5f6789abcdef0"
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Friend request accepted successfully",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Friend request accepted successfully")
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request - Request already processed",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "This friend request has already been processed")
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Authentication required",
                    content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "Authentication required"))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - Not authorized to accept this request",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "You are not authorized to accept this friend request")
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found - Friend request not found",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Friend request not found")
                    )
            )
    })
    public @interface AcceptFriendRequestDoc {}

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            tags = {"User"},
            summary = "Reject friend request",
            description = "Rejects a pending friend request. Only the recipient can reject the request.",
            security = @SecurityRequirement(name = "bearerAuth"),
            parameters = {
                    @Parameter(
                            name = "requestId",
                            description = "ID of the friend request to reject",
                            required = true,
                            example = "64a1b2c3d4e5f6789abcdef0"
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Friend request rejected successfully",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Friend request rejected successfully")
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request - Request already processed",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "This friend request has already been processed")
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Authentication required",
                    content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "Authentication required"))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - Not authorized to reject this request",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "You are not authorized to reject this friend request")
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found - Friend request not found",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Friend request not found")
                    )
            )
    })
    public @interface RejectFriendRequestDoc {}

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            tags = {"User"},
            summary = "Cancel friend request",
            description = "Cancels a pending friend request that was sent by the authenticated user.",
            security = @SecurityRequirement(name = "bearerAuth"),
            parameters = {
                    @Parameter(
                            name = "requestId",
                            description = "ID of the friend request to cancel",
                            required = true,
                            example = "64a1b2c3d4e5f6789abcdef0"
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Friend request canceled successfully",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Friend request canceled successfully")
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request - Request already processed",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "This friend request has already been processed")
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Authentication required",
                    content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "Authentication required"))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - Not authorized to cancel this request",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "You are not authorized to cancel this friend request")
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found - Friend request not found",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Friend request not found")
                    )
            )
    })
    public @interface CancelFriendRequestDoc {}

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            tags = {"User"},
            summary = "Get friends list",
            description = "Retrieves the authenticated user's friends list. " +
                    "Can be filtered by username prefix. Returns total friend count and filtered results.",
            security = @SecurityRequirement(name = "bearerAuth"),
            parameters = {
                    @Parameter(
                            name = "prefix",
                            description = "Username prefix to filter friends (optional)",
                            required = false,
                            examples = {
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(name = "Filter by prefix", value = "john"),
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(name = "All friends", value = "")
                            }
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Friends list retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = com.supinfo.beunreal_api_gateway.model.user.response.GetFriendsResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "With Friends",
                                            summary = "User has friends",
                                            value = """
                                                    {
                                                        "totalFriendsFound": 5,
                                                        "friends": [
                                                            {
                                                                "id": "64a1b2c3d4e5f6789abcdef0",
                                                                "username": "johndoe",
                                                                "profilePicture": "https://example.com/avatar1.jpg",
                                                                "status": "ONLINE"
                                                            },
                                                            {
                                                                "id": "64a1b2c3d4e5f6789abcdef1",
                                                                "username": "janedoe",
                                                                "profilePicture": "https://example.com/avatar2.jpg",
                                                                "status": "OFFLINE"
                                                            }
                                                        ]
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "No Friends",
                                            summary = "User has no friends",
                                            value = """
                                                    {
                                                        "totalFriendsFound": 0,
                                                        "friends": []
                                                    }
                                                    """
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Authentication required",
                    content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "Authentication required"))
            )
    })
    public @interface GetFriendsDoc {}


    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            tags = {"User"},
            summary = "Create a new group",
            description = "Creates a new group with the authenticated user as the creator and admin. " +
                    "Group name must be 3-20 characters and group picture must be a valid HTTPS image URL.",
            security = @SecurityRequirement(name = "bearerAuth"),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Group creation information",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = com.supinfo.beunreal_api_gateway.model.user.request.CreateGroupRequest.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Project Group",
                                            summary = "Create a project group",
                                            value = """
                                                    {
                                                        "name": "Project Team Alpha",
                                                        "groupPicture": "https://example.com/group-avatar.jpg"
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Social Group",
                                            summary = "Create a social group",
                                            value = """
                                                    {
                                                        "name": "Weekend Warriors",
                                                        "groupPicture": "https://example.com/social-group.png"
                                                    }
                                                    """
                                    )
                            }
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Group created successfully",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Group created successfully!")
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request - Validation errors",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = {
                                    @ExampleObject(
                                            name = "Invalid Name",
                                            summary = "Group name validation failed",
                                            value = "Group name must be at least 3 characters long"
                                    ),
                                    @ExampleObject(
                                            name = "Invalid Picture URL",
                                            summary = "Group picture URL validation failed",
                                            value = "Group picture URL must start with 'https://'. Group picture must be a valid image format (jpg, jpeg, png, gif, webp, or svg)"
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Authentication required",
                    content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "Authentication required"))
            )
    })
    public @interface CreateGroupDoc {}

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            tags = {"User"},
            summary = "Update group information",
            description = "Updates group information. Only group admins can update group details. " +
                    "At least one field (name or groupPicture) must be provided.",
            security = @SecurityRequirement(name = "bearerAuth"),
            parameters = {
                    @Parameter(
                            name = "groupId",
                            description = "ID of the group to update",
                            required = true,
                            example = "64a1b2c3d4e5f6789abcdef0"
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Group information to update",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = com.supinfo.beunreal_api_gateway.model.user.request.UpdateGroupRequest.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Update Name Only",
                                            summary = "Update only group name",
                                            value = """
                                                    {
                                                        "name": "New Project Team Name"
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Update Picture Only",
                                            summary = "Update only group picture",
                                            value = """
                                                    {
                                                        "groupPicture": "https://example.com/new-group-avatar.jpg"
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Update Both",
                                            summary = "Update both name and picture",
                                            value = """
                                                    {
                                                        "name": "Updated Team Name",
                                                        "groupPicture": "https://example.com/updated-avatar.png"
                                                    }
                                                    """
                                    )
                            }
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Group updated successfully",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Group updated successfully!")
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request - Validation errors",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = {
                                    @ExampleObject(
                                            name = "No Fields",
                                            summary = "No fields provided",
                                            value = "At least one field (name or group picture) must be provided"
                                    ),
                                    @ExampleObject(
                                            name = "Invalid Name",
                                            summary = "Group name validation failed",
                                            value = "Group name cannot exceed 20 characters"
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Not authorized to update this group",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "You are not authorized to update this group")
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found - Group not found",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Group not found")
                    )
            )
    })
    public @interface UpdateGroupDoc {}

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            tags = {"User"},
            summary = "Delete group",
            description = "Permanently deletes a group and all associated data. " +
                    "Only the group creator can delete the group. This action cannot be undone.",
            security = @SecurityRequirement(name = "bearerAuth"),
            parameters = {
                    @Parameter(
                            name = "groupId",
                            description = "ID of the group to delete",
                            required = true,
                            example = "64a1b2c3d4e5f6789abcdef0"
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Group deleted successfully",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Group deleted successfully!")
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Not authorized to delete this group",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "You are not authorized to delete this group")
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found - Group not found",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Group not found")
                    )
            )
    })
    public @interface DeleteGroupDoc {}

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            tags = {"User"},
            summary = "Get group information",
            description = "Retrieves detailed information about a group including members, admins, and creator. " +
                    "Only group members can access group information.",
            security = @SecurityRequirement(name = "bearerAuth"),
            parameters = {
                    @Parameter(
                            name = "groupId",
                            description = "ID of the group to retrieve",
                            required = true,
                            example = "64a1b2c3d4e5f6789abcdef0"
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Group information retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = com.supinfo.beunreal_api_gateway.model.user.response.GetGroupInfoResponse.class),
                            examples = @ExampleObject(
                                    name = "Group Details",
                                    value = """
                                            {
                                                "id": "64a1b2c3d4e5f6789abcdef0",
                                                "name": "Project Team Alpha",
                                                "creator": {
                                                    "id": "64a1b2c3d4e5f6789abcdef1",
                                                    "username": "teamlead",
                                                    "profilePicture": "https://example.com/lead-avatar.jpg",
                                                    "status": "ONLINE"
                                                },
                                                "groupPicture": "https://example.com/group-avatar.jpg",
                                                "members": [
                                                    {
                                                        "id": "64a1b2c3d4e5f6789abcdef2",
                                                        "username": "developer1",
                                                        "profilePicture": "https://example.com/dev1.jpg",
                                                        "status": "ONLINE"
                                                    }
                                                ],
                                                "admins": [
                                                    {
                                                        "id": "64a1b2c3d4e5f6789abcdef1",
                                                        "username": "teamlead",
                                                        "profilePicture": "https://example.com/lead-avatar.jpg",
                                                        "status": "ONLINE"
                                                    }
                                                ],
                                                "createdAt": "2024-01-15T10:30:00",
                                                "updatedAt": "2024-01-16T14:20:00"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Not a group member",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Access denied\"}"))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found - Group not found",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Group not found\"}"))
            )
    })
    public @interface GetGroupInfoDoc {}

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            tags = {"User"},
            summary = "Remove user from group",
            description = "Removes a user from the group. Group admins can remove regular members. " +
                    "Only the group creator can remove other admins.",
            security = @SecurityRequirement(name = "bearerAuth"),
            parameters = {
                    @Parameter(
                            name = "groupId",
                            description = "ID of the group",
                            required = true,
                            example = "64a1b2c3d4e5f6789abcdef0"
                    ),
                    @Parameter(
                            name = "userId",
                            description = "ID of the user to remove from group",
                            required = true,
                            example = "64a1b2c3d4e5f6789abcdef1"
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User removed from group successfully",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "User removed successfully from the group!")
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Insufficient permissions",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = {
                                    @ExampleObject(
                                            name = "Not Admin",
                                            summary = "User is not a group admin",
                                            value = "You are not authorized to remove a user from this group"
                                    ),
                                    @ExampleObject(
                                            name = "Cannot Remove Admin",
                                            summary = "Cannot remove admin without creator privileges",
                                            value = "You are not authorized to remove this user from this group"
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found - Group or user not found",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = {
                                    @ExampleObject(name = "Group Not Found", value = "Group not found"),
                                    @ExampleObject(name = "User Not Found", value = "User not found")
                            }
                    )
            )
    })
    public @interface RemoveUserFromGroupDoc {}

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            tags = {"User"},
            summary = "Add user to group",
            description = "Sends a group invitation to add a user to the group. " +
                    "Only group admins can invite users. User must not already be a member.",
            security = @SecurityRequirement(name = "bearerAuth"),
            parameters = {
                    @Parameter(
                            name = "groupId",
                            description = "ID of the group",
                            required = true,
                            example = "64a1b2c3d4e5f6789abcdef0"
                    ),
                    @Parameter(
                            name = "userId",
                            description = "ID of the user to invite to group",
                            required = true,
                            example = "64a1b2c3d4e5f6789abcdef1"
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Group invitation sent successfully",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Group invitation has been sent to johndoe")
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request - User already in group",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "User is already in the group")
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Not authorized to invite users",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "You are not authorized to add a user to this group")
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found - Group or user not found",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = {
                                    @ExampleObject(name = "Group Not Found", value = "Group not found"),
                                    @ExampleObject(name = "User Not Found", value = "User not found")
                            }
                    )
            )
    })
    public @interface AddUserToGroupDoc {}


    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            tags = {"User"},
            summary = "Get group invitations",
            description = "Retrieves all invitations for a specific group. " +
                    "Only group members can view group invitations.",
            security = @SecurityRequirement(name = "bearerAuth"),
            parameters = {
                    @Parameter(
                            name = "groupId",
                            description = "ID of the group to get invitations for",
                            required = true,
                            example = "64a1b2c3d4e5f6789abcdef0"
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Group invitations retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = com.supinfo.beunreal_api_gateway.model.user.response.GetGroupInvitationsResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "With Invitations",
                                            summary = "Group has pending invitations",
                                            value = """
                                                    {
                                                        "groupInvitations": [
                                                            {
                                                                "sender": {
                                                                    "id": "64a1b2c3d4e5f6789abcdef1",
                                                                    "username": "teamlead",
                                                                    "profilePicture": "https://example.com/lead.jpg"
                                                                },
                                                                "receiver": {
                                                                    "id": "64a1b2c3d4e5f6789abcdef2",
                                                                    "username": "newdev",
                                                                    "profilePicture": "https://example.com/newdev.jpg"
                                                                },
                                                                "sentDate": "2024-01-15T10:30:00",
                                                                "status": "PENDING"
                                                            }
                                                        ]
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "No Invitations",
                                            summary = "No pending invitations",
                                            value = """
                                                    {
                                                        "groupInvitations": []
                                                    }
                                                    """
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Not a group member",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Access denied\"}"))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found - Group not found",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Group not found\"}"))
            )
    })
    public @interface GetGroupInvitationsDoc {}

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            tags = {"User"},
            summary = "Get user's group invitations",
            description = "Retrieves all pending group invitations received by the authenticated user.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User group invitations retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = com.supinfo.beunreal_api_gateway.model.user.response.GetUserGroupInvitationsResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "With Invitations",
                                            summary = "User has pending group invitations",
                                            value = """
                                                    {
                                                        "userGroupInvitations": [
                                                            {
                                                                "sender": {
                                                                    "id": "64a1b2c3d4e5f6789abcdef1",
                                                                    "username": "teamlead",
                                                                    "profilePicture": "https://example.com/lead.jpg"
                                                                },
                                                                "group": {
                                                                    "id": "64a1b2c3d4e5f6789abcdef0",
                                                                    "name": "Project Team Alpha",
                                                                    "groupPicture": "https://example.com/group.jpg"
                                                                },
                                                                "sentDate": "2024-01-15T10:30:00"
                                                            }
                                                        ]
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "No Invitations",
                                            summary = "No pending invitations",
                                            value = """
                                                    {
                                                        "userGroupInvitations": []
                                                    }
                                                    """
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Authentication required",
                    content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "Authentication required"))
            )
    })
    public @interface GetUserGroupInvitationsDoc {}

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            tags = {"User"},
            summary = "Reject group invitation",
            description = "Rejects a pending group invitation. Only the invitation recipient can reject it.",
            security = @SecurityRequirement(name = "bearerAuth"),
            parameters = {
                    @Parameter(
                            name = "groupId",
                            description = "ID of the group invitation to reject",
                            required = true,
                            example = "64a1b2c3d4e5f6789abcdef0"
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Group invitation rejected successfully",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Group invitation rejected successfully")
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request - Invitation already processed",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "This invitation has already been processed")
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Authentication required",
                    content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "Authentication required"))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - Not authorized to reject this invitation",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "You are not authorized to reject this invitation")
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found - Group invitation not found",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Group invitation not found")
                    )
            )
    })
    public @interface RejectGroupInvitationDoc {}

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            tags = {"User"},
            summary = "Accept group invitation",
            description = "Accepts a pending group invitation and joins the group. " +
                    "Only the invitation recipient can accept it.",
            security = @SecurityRequirement(name = "bearerAuth"),
            parameters = {
                    @Parameter(
                            name = "groupId",
                            description = "ID of the group invitation to accept",
                            required = true,
                            example = "64a1b2c3d4e5f6789abcdef0"
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Group invitation accepted successfully",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Group invitation accepted successfully")
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request - Invitation already processed or user already in group",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = {
                                    @ExampleObject(
                                            name = "Already Processed",
                                            summary = "Invitation already processed",
                                            value = "This invitation has already been processed"
                                    ),
                                    @ExampleObject(
                                            name = "Already Member",
                                            summary = "User already in group",
                                            value = "You are already a member of this group"
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Authentication required",
                    content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "Authentication required"))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - Not authorized to accept this invitation",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "You are not authorized to accept this invitation")
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found - Group invitation or group not found",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = {
                                    @ExampleObject(name = "Invitation Not Found", value = "Group invitation not found"),
                                    @ExampleObject(name = "Group Not Found", value = "The group no longer exists")
                            }
                    )
            )
    })
    public @interface AcceptGroupInvitationDoc {}

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            tags = {"User"},
            summary = "Cancel group invitation",
            description = "Cancels a pending group invitation. Only group admins can cancel invitations for their group.",
            security = @SecurityRequirement(name = "bearerAuth"),
            parameters = {
                    @Parameter(
                            name = "groupId",
                            description = "ID of the group",
                            required = true,
                            example = "64a1b2c3d4e5f6789abcdef0"
                    ),
                    @Parameter(
                            name = "invitationId",
                            description = "ID of the invitation to cancel",
                            required = true,
                            example = "64a1b2c3d4e5f6789abcdef1"
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Group invitation canceled successfully",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Group invitation canceled successfully")
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request - Invitation already processed or doesn't belong to group",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = {
                                    @ExampleObject(
                                            name = "Already Processed",
                                            summary = "Invitation already processed",
                                            value = "This invitation has already been processed"
                                    ),
                                    @ExampleObject(
                                            name = "Wrong Group",
                                            summary = "Invitation doesn't belong to specified group",
                                            value = "This invitation does not belong to the specified group"
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Authentication required",
                    content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "Authentication required"))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - Not authorized to cancel invitations for this group",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "You are not authorized to cancel invitations for this group")
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found - Group or invitation not found",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = {
                                    @ExampleObject(name = "Group Not Found", value = "Group not found"),
                                    @ExampleObject(name = "Invitation Not Found", value = "Invitation not found")
                            }
                    )
            )
    })
    public @interface CancelGroupInvitationDoc {}

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            tags = {"User"},
            summary = "Get user's groups",
            description = "Retrieves all groups that the authenticated user is a member of.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User groups retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = com.supinfo.beunreal_api_gateway.model.user.response.GetUserGroupsResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "With Groups",
                                            summary = "User is member of groups",
                                            value = """
                                                    {
                                                        "groups": [
                                                            {
                                                                "id": "64a1b2c3d4e5f6789abcdef0",
                                                                "name": "Project Team Alpha",
                                                                "groupPicture": "https://example.com/group1.jpg"
                                                            },
                                                            {
                                                                "id": "64a1b2c3d4e5f6789abcdef1",
                                                                "name": "Weekend Warriors",
                                                                "groupPicture": "https://example.com/group2.jpg"
                                                            }
                                                        ]
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "No Groups",
                                            summary = "User is not member of any groups",
                                            value = """
                                                    {
                                                        "groups": []
                                                    }
                                                    """
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Authentication required",
                    content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "Authentication required"))
            )
    })
    public @interface GetUserGroupsDoc {}

}