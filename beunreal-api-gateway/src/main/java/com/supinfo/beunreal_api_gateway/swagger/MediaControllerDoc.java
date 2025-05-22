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

public class MediaControllerDoc {

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            tags = {"Media"},
            summary = "Share media to a group",
            description = "Shares an existing media (image or video) to a specified group. " +
                    "Both the media and group must exist, and the user must be a member of the group.",
            security = @SecurityRequirement(name = "bearerAuth"),
            parameters = {
                    @Parameter(
                            name = "mediaId",
                            description = "Unique identifier of the media to share",
                            required = true,
                            example = "64a1b2c3d4e5f6789abcdef0"
                    ),
                    @Parameter(
                            name = "groupId",
                            description = "Unique identifier of the target group",
                            required = true,
                            example = "64a1b2c3d4e5f6789abcdef1"
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Media shared successfully",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Media shared successfully")
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request - Invalid media or group ID",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Invalid media or group ID")
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
                    description = "Forbidden - User not member of the group",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Access denied: You are not a member of this group")
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found - Media or group not found",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Media or group not found")
                    )
            )
    })
    public @interface ShareMediaToGroupDoc {}

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            tags = {"Media"},
            summary = "Share media to a user",
            description = "Shares an existing media (image or video) to a specific user. " +
                    "Both the media and target user must exist, and users should be friends.",
            security = @SecurityRequirement(name = "bearerAuth"),
            parameters = {
                    @Parameter(
                            name = "mediaId",
                            description = "Unique identifier of the media to share",
                            required = true,
                            example = "64a1b2c3d4e5f6789abcdef0"
                    ),
                    @Parameter(
                            name = "userId",
                            description = "Unique identifier of the target user",
                            required = true,
                            example = "64a1b2c3d4e5f6789abcdef2"
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Media shared successfully",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Media shared successfully")
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request - Invalid media or user ID",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Invalid media or user ID")
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
                    description = "Not Found - Media or user not found",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Media or user not found")
                    )
            )
    })
    public @interface ShareMediaToUserDoc {}

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            tags = {"Media"},
            summary = "Upload media file and get URL",
            description = "Uploads an image or video file to cloud storage and returns a secure HTTPS URL. " +
                    "Supported formats: JPEG, PNG, GIF, WebP for images; MP4, AVI, MOV, WMV, WebM for videos. " +
                    "Maximum file size: 50MB.",
            security = @SecurityRequirement(name = "bearerAuth"),
            parameters = {
                    @Parameter(
                            name = "file",
                            description = "Media file to upload (image or video)",
                            required = true,
                            content = @Content(mediaType = "multipart/form-data")
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "File uploaded successfully - Returns HTTPS URL",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    name = "Upload Success",
                                    value = "https://res.cloudinary.com/demo/image/upload/v1234567890/sample.jpg"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request - File validation errors",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = {
                                    @ExampleObject(
                                            name = "No File",
                                            summary = "No file provided",
                                            value = "No file provided"
                                    ),
                                    @ExampleObject(
                                            name = "File Too Large",
                                            summary = "File size exceeds limit",
                                            value = "File size exceeds maximum limit of 50MB"
                                    ),
                                    @ExampleObject(
                                            name = "Unsupported Format",
                                            summary = "File format not supported",
                                            value = "File type not supported. Please upload an image (JPEG, PNG, GIF, WebP) or video (MP4, AVI, MOV, WMV, WebM)"
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error - Upload failed",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = {
                                    @ExampleObject(
                                            name = "Upload Failed",
                                            summary = "Upload operation failed",
                                            value = "Failed to upload media. Please try again later."
                                    ),
                                    @ExampleObject(
                                            name = "Unexpected Error",
                                            summary = "Unexpected server error",
                                            value = "An unexpected error occurred. Please try again later."
                                    )
                            }
                    )
            )
    })
    public @interface GetMediaUrlDoc {}

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            tags = {"Media"},
            summary = "Get nearby media discoveries",
            description = "Retrieves media (stories) posted by users near the specified location. " +
                    "Returns media within approximately 10km radius of the given coordinates. " +
                    "Only non-expired media are included in the results.",
            security = @SecurityRequirement(name = "bearerAuth"),
            parameters = {
                    @Parameter(
                            name = "latitude",
                            description = "Latitude coordinate (-90 to 90)",
                            required = true,
                            example = "48.8566"
                    ),
                    @Parameter(
                            name = "longitude",
                            description = "Longitude coordinate (-180 to 180)",
                            required = true,
                            example = "2.3522"
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Nearby discoveries retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = com.supinfo.beunreal_api_gateway.model.discovery.response.GetNearbyDiscoveriesResponse.class),
                            examples = @ExampleObject(
                                    name = "Nearby Stories",
                                    value = """
                                            {
                                                "stories": [
                                                    {
                                                        "id": "64a1b2c3d4e5f6789abcdef0",
                                                        "creator": {
                                                            "id": "64a1b2c3d4e5f6789abcdef1",
                                                            "username": "johndoe",
                                                            "profilePicture": "https://example.com/avatar.jpg",
                                                            "isFriend": false
                                                        },
                                                        "mediaUrl": "https://res.cloudinary.com/demo/image/upload/sample.jpg",
                                                        "location": {
                                                            "latitude": 48.8566,
                                                            "longitude": 2.3522
                                                        },
                                                        "createdAt": "2024-01-15T10:30:00"
                                                    }
                                                ]
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request - Invalid coordinates",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\": \"Invalid coordinates provided\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Authentication required",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Authentication required")
                    )
            )
    })
    public @interface GetNearbyDiscoveriesDoc {}

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            tags = {"Media"},
            summary = "Post a new story",
            description = "Creates a new media story with the provided URL and location. " +
                    "The media URL should be obtained from the upload endpoint first. " +
                    "Stories expire after 24 hours and are visible to nearby users.",
            security = @SecurityRequirement(name = "bearerAuth"),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Story creation request with media URL and location",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = com.supinfo.beunreal_api_gateway.model.discovery.request.PostMediaRequest.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Image Story",
                                            summary = "Post an image story",
                                            value = """
                                                    {
                                                        "mediaUrl": "https://res.cloudinary.com/demo/image/upload/v1234567890/sample.jpg",
                                                        "location": {
                                                            "latitude": 48.8566,
                                                            "longitude": 2.3522
                                                        }
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Video Story",
                                            summary = "Post a video story",
                                            value = """
                                                    {
                                                        "mediaUrl": "https://res.cloudinary.com/demo/video/upload/v1234567890/sample.mp4",
                                                        "location": {
                                                            "latitude": 40.7128,
                                                            "longitude": -74.0060
                                                        }
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
                    description = "Story posted successfully",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "Media posted successfully")
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request - Invalid request data",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = {
                                    @ExampleObject(
                                            name = "Missing Media URL",
                                            value = "Media URL is required"
                                    ),
                                    @ExampleObject(
                                            name = "Invalid Location",
                                            value = "Invalid location coordinates"
                                    ),
                                    @ExampleObject(
                                            name = "Invalid Media URL",
                                            value = "Invalid media URL format"
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
            )
    })
    public @interface PostStoryDoc {}

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            tags = {"Media"},
            summary = "Get media details by ID",
            description = "Retrieves detailed information about a specific media item including " +
                    "the media URL, creation date, and creator information.",
            security = @SecurityRequirement(name = "bearerAuth"),
            parameters = {
                    @Parameter(
                            name = "mediaId",
                            description = "Unique identifier of the media",
                            required = true,
                            example = "64a1b2c3d4e5f6789abcdef0"
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Media details retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = com.supinfo.beunreal_api_gateway.model.media.response.GetMediaResponse.class),
                            examples = @ExampleObject(
                                    name = "Media Details",
                                    value = """
                                            {
                                                "mediaUrl": "https://res.cloudinary.com/demo/image/upload/v1234567890/sample.jpg",
                                                "createdAt": "2024-01-15T10:30:00",
                                                "creator": {
                                                    "id": "64a1b2c3d4e5f6789abcdef1",
                                                    "username": "johndoe",
                                                    "profilePicture": "https://example.com/avatar.jpg",
                                                    "isFriend": false
                                                }
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found - Media or creator not found",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Media Not Found",
                                            value = "{\"error\": \"Media not found\"}"
                                    ),
                                    @ExampleObject(
                                            name = "Creator Not Found",
                                            value = "{\"error\": \"Media creator not found\"}"
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
            )
    })
    public @interface GetMediaDoc {}
}