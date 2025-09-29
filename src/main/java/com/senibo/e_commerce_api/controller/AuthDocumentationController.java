package com.senibo.e_commerce_api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "oauth-documentation-controller", description = "Endpoints for user authentication " +
        "and OAuth2 flows")
public class AuthDocumentationController {

    /**
     * This is a documentation-only endpoint. It describes the initiation of the Google OAuth2 flow.
     * Making a GET request to this endpoint will trigger Spring Security's OAuth2 filter
     * and redirect the user to the Google consent screen.
     */
    @Operation(
            summary = "Initiate Google OAuth2 Login",
            description = "Initiates the OAuth2 authorization code flow with Google. This endpoint is handled by Spring Security's filter chain. " +
                    "Accessing this URL will result in a 302 redirect to the Google authentication page. " +
                    "After successful authentication, the user will be redirected back to the frontend application at the configured redirect URI, with a JWT token appended as a query parameter.",
            responses = {
                    @ApiResponse(
                            responseCode = "302",
                            description = "Redirect to Google's OAuth2 consent screen. The 'Location' header will contain the redirect URL."
                    )
            }
    )
    @GetMapping("/oauth2/authorization/google")
    public void initiateGoogleLogin() {
        // This method will never be executed. The Spring Security filter intercepts the request before it reaches this controller.
        // This endpoint is defined here purely for OpenAPI documentation purposes.
    }
}
