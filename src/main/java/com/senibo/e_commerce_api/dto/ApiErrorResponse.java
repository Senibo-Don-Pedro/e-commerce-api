package com.senibo.e_commerce_api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Standard error response format for API operations")
public record ApiErrorResponse(
        @Schema(description = "Indicates if the API call was successful. Always `false` for errors.",
                example = "false")
        boolean success,

        @Schema(description = "Human-readable error message describing what went wrong")
        String message,

        @Schema(description = "Optional list of detailed error messages or validation errors",
                example = "[\"NOTE THIS ARRAY COULD BE NULL\"]")
        List<String> errors
) {
}
