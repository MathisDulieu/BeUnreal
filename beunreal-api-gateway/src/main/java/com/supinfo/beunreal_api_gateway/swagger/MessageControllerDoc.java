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

public class MessageControllerDoc {

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            tags = {"Message"},
            summary = "Send a private message",
            description = "Sends a private message to a specific user. The recipient user must exist. " +
                    "Messages are delivered asynchronously through the messaging system.",
            security = @SecurityRequirement(name = "bearerAuth"),
            parameters = {
                    @Parameter(
                            name = "userId",
                            description = "Unique identifier of the message recipient",
                            required = true,
                            example = "64a1b2c3d4e5f6789abcdef0"
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Message content to send",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = com.supinfo.beunreal_api_gateway.model.message.request.SendPrivateMessageRequest.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Text Message",
                                            summary = "Simple text message",
                                            value = """
                                                    {
                                                        "content": "Hello! How are you doing today?"
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Long Message",
                                            summary = "Longer message with multiple sentences",
                                            value = """
                                                    {
                                                        "content": "Hey there! I hope you're having a great day. I wanted to follow up on our conversation from yesterday about the project. Let me know when you have time to chat!"
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
                    description = "Message sent successfully",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Private message send successfully")
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request - Invalid message content or user ID",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = {
                                    @ExampleObject(
                                            name = "Empty Content",
                                            summary = "Message content is empty",
                                            value = "Message content cannot be empty"
                                    ),
                                    @ExampleObject(
                                            name = "Invalid User ID",
                                            summary = "Invalid recipient user ID format",
                                            value = "Invalid user ID format"
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Authentication required",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Authentication required")
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found - Recipient user not found",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Recipient user not found")
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error - Message delivery failed",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Failed to send message. Please try again later.")
                    )
            )
    })
    public @interface SendPrivateMessageDoc {}

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            tags = {"Message"},
            summary = "Get private conversation",
            description = "Retrieves the entire conversation history between the authenticated user and a specific user. " +
                    "Messages are returned in descending order (newest first). If no conversation exists, " +
                    "returns empty messages list with friend information.",
            security = @SecurityRequirement(name = "bearerAuth"),
            parameters = {
                    @Parameter(
                            name = "userId",
                            description = "Unique identifier of the conversation partner",
                            required = true,
                            example = "64a1b2c3d4e5f6789abcdef0"
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Conversation retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = com.supinfo.beunreal_api_gateway.model.message.response.GetPrivateConversationResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Active Conversation",
                                            summary = "Conversation with message history",
                                            value = """
                                                    {
                                                        "friend": {
                                                            "id": "64a1b2c3d4e5f6789abcdef0",
                                                            "username": "johndoe",
                                                            "profilePicture": "https://example.com/avatar.jpg",
                                                            "status": "ONLINE"
                                                        },
                                                        "messages": [
                                                            {
                                                                "id": "64a1b2c3d4e5f6789abcdef1",
                                                                "sender": {
                                                                    "id": "64a1b2c3d4e5f6789abcdef2",
                                                                    "username": "alice",
                                                                    "profilePicture": "https://example.com/alice.jpg",
                                                                    "status": "ONLINE"
                                                                },
                                                                "receiver": {
                                                                    "id": "64a1b2c3d4e5f6789abcdef0",
                                                                    "username": "johndoe",
                                                                    "profilePicture": "https://example.com/avatar.jpg",
                                                                    "status": "ONLINE"
                                                                },
                                                                "content": "Hello! How are you?",
                                                                "mediaUrl": null,
                                                                "sentAt": "2024-01-15T10:30:00",
                                                                "status": "READ"
                                                            }
                                                        ]
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Empty Conversation",
                                            summary = "No messages yet between users",
                                            value = """
                                                    {
                                                        "friend": {
                                                            "id": "64a1b2c3d4e5f6789abcdef0",
                                                            "username": "johndoe",
                                                            "profilePicture": "https://example.com/avatar.jpg",
                                                            "status": "OFFLINE"
                                                        },
                                                        "messages": []
                                                    }
                                                    """
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Authentication required",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Authentication required")
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found - User not found",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\": \"User not found\"}")
                    )
            )
    })
    public @interface GetPrivateConversationDoc {}

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            tags = {"Message"},
            summary = "Send a group message",
            description = "Sends a message to a group chat. The authenticated user must be a member of the group. " +
                    "The message is delivered to all group members asynchronously.",
            security = @SecurityRequirement(name = "bearerAuth"),
            parameters = {
                    @Parameter(
                            name = "groupId",
                            description = "Unique identifier of the target group",
                            required = true,
                            example = "64a1b2c3d4e5f6789abcdef0"
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Message content to send to the group",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = com.supinfo.beunreal_api_gateway.model.message.request.SendGroupMessageRequest.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Group Announcement",
                                            summary = "Announcement to group members",
                                            value = """
                                                    {
                                                        "content": "Hey everyone! Don't forget about our meeting tomorrow at 3 PM."
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Casual Message",
                                            summary = "Casual group conversation",
                                            value = """
                                                    {
                                                        "content": "Anyone up for lunch today?"
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
                    description = "Group message sent successfully",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Group message send successfully")
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request - Invalid message content or group ID",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = {
                                    @ExampleObject(
                                            name = "Empty Content",
                                            summary = "Message content is empty",
                                            value = "Message content cannot be empty"
                                    ),
                                    @ExampleObject(
                                            name = "Invalid Group ID",
                                            summary = "Invalid group ID format",
                                            value = "Invalid group ID format"
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Authentication required",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Authentication required")
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - User not a member of the group",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Access denied: You are not a member of this group")
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found - Group not found",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Group not found")
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error - Message delivery failed",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Failed to send group message. Please try again later.")
                    )
            )
    })
    public @interface SendGroupMessageDoc {}

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            tags = {"Message"},
            summary = "Get group conversation",
            description = "Retrieves the entire conversation history for a specific group. " +
                    "The authenticated user must be a member of the group to access the conversation. " +
                    "Messages are returned in descending order (newest first) along with group information.",
            security = @SecurityRequirement(name = "bearerAuth"),
            parameters = {
                    @Parameter(
                            name = "groupId",
                            description = "Unique identifier of the group",
                            required = true,
                            example = "64a1b2c3d4e5f6789abcdef0"
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Group conversation retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = com.supinfo.beunreal_api_gateway.model.message.response.GetGroupConversationResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Active Group Chat",
                                            summary = "Group with message history",
                                            value = """
                                                    {
                                                        "group": {
                                                            "id": "64a1b2c3d4e5f6789abcdef0",
                                                            "name": "Project Team",
                                                            "groupPicture": "https://example.com/group-avatar.jpg"
                                                        },
                                                        "messages": [
                                                            {
                                                                "id": "64a1b2c3d4e5f6789abcdef1",
                                                                "sender": {
                                                                    "id": "64a1b2c3d4e5f6789abcdef2",
                                                                    "username": "alice",
                                                                    "profilePicture": "https://example.com/alice.jpg",
                                                                    "status": "ONLINE"
                                                                },
                                                                "content": "Great work on the presentation everyone!",
                                                                "mediaUrl": null,
                                                                "sentAt": "2024-01-15T14:30:00",
                                                                "status": "DELIVERED"
                                                            },
                                                            {
                                                                "id": "64a1b2c3d4e5f6789abcdef3",
                                                                "sender": {
                                                                    "id": "64a1b2c3d4e5f6789abcdef4",
                                                                    "username": "bob",
                                                                    "profilePicture": "https://example.com/bob.jpg",
                                                                    "status": "OFFLINE"
                                                                },
                                                                "content": "Thanks! When is our next meeting?",
                                                                "mediaUrl": null,
                                                                "sentAt": "2024-01-15T14:25:00",
                                                                "status": "READ"
                                                            }
                                                        ]
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Empty Group Chat",
                                            summary = "Group with no messages yet",
                                            value = """
                                                    {
                                                        "group": {
                                                            "id": "64a1b2c3d4e5f6789abcdef0",
                                                            "name": "New Project Team",
                                                            "groupPicture": "https://example.com/default-group.jpg"
                                                        },
                                                        "messages": []
                                                    }
                                                    """
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Authentication required",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Authentication required")
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - User not a member of the group",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\": \"Access denied: You are not a member of this group\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found - Group not found",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\": \"Group not found\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error - Failed to retrieve conversation",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Failed to retrieve group conversation. Please try again later.")
                    )
            )
    })
    public @interface GetGroupConversationDoc {}
}