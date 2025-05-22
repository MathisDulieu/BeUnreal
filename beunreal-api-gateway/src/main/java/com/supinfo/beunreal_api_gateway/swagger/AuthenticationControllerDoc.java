package com.supinfo.beunreal_api_gateway.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class AuthenticationControllerDoc {

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            tags = {"Authentication"},
            summary = "Register a new user account",
            description = "Creates a new user account with the provided username, email, and password. " +
                    "All fields are required and must meet validation criteria.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User registration information",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = com.supinfo.beunreal_api_gateway.model.authentication.RegisterRequest.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Valid Registration",
                                            summary = "Example of a valid registration request",
                                            value = """
                                                    {
                                                        "username": "johndoe123",
                                                        "email": "john.doe@example.com",
                                                        "password": "SecurePass123!"
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Minimum Requirements",
                                            summary = "Example with minimum requirements",
                                            value = """
                                                    {
                                                        "username": "usr",
                                                        "email": "user@domain.com",
                                                        "password": "MinPass1!"
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
                    description = "User registered successfully",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    value = "Registration successful! You can now log in to your account."
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request - Validation errors",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = {
                                    @ExampleObject(
                                            name = "Invalid Email",
                                            summary = "Invalid email format",
                                            value = "Invalid email format. Please enter a valid email address."
                                    ),
                                    @ExampleObject(
                                            name = "Invalid Username",
                                            summary = "Username validation failed",
                                            value = "Invalid username. Username must be between 3-12 characters and contain only letters, numbers, and underscores."
                                    ),
                                    @ExampleObject(
                                            name = "Weak Password",
                                            summary = "Password does not meet requirements",
                                            value = "Password does not meet security requirements. Please use at least 8 characters including uppercase, lowercase, numbers, and special characters."
                                    ),
                                    @ExampleObject(
                                            name = "Username Taken",
                                            summary = "Username already exists",
                                            value = "Username already exists. Please choose a different username."
                                    ),
                                    @ExampleObject(
                                            name = "Email Taken",
                                            summary = "Email already registered",
                                            value = "Email address is already registered. Please use a different email or try to recover your account."
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error - Unexpected server error",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    value = "An unexpected error occurred. Please try again later."
                            )
                    )
            )
    })
    public @interface RegisterDoc {}

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            tags = {"Authentication"},
            summary = "Login to user account",
            description = "Authenticates a user with email and password, returning a JWT token for subsequent requests. " +
                    "The token should be included in the Authorization header as 'Bearer {token}' for protected endpoints.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User login credentials",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = com.supinfo.beunreal_api_gateway.model.authentication.LoginRequest.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Valid Login",
                                            summary = "Example of a valid login request",
                                            value = """
                                                    {
                                                        "email": "john.doe@example.com",
                                                        "password": "SecurePass123!"
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Basic Login",
                                            summary = "Simple login example",
                                            value = """
                                                    {
                                                        "email": "user@domain.com",
                                                        "password": "userpassword"
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
                    description = "Login successful - JWT token returned",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    name = "JWT Token",
                                    summary = "Successful login returns JWT token",
                                    value = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiI2NzQxYWY4ZjVmZjc4YTBjMjkxY2ZlMmUiLCJpYXQiOjE3MzM5NzUxMTksImV4cCI6MTczNDA2MTUxOX0.example_token_signature"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request - Invalid password",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    name = "Wrong Password",
                                    summary = "Incorrect password provided",
                                    value = "Invalid password. Please try again."
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found - User account not found",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    name = "User Not Found",
                                    summary = "No account found with provided email",
                                    value = "User not found. Please check your email address or register for a new account."
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error - Unexpected server error",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    value = "An unexpected error occurred. Please try again later."
                            )
                    )
            )
    })
    public @interface LoginDoc {}
}