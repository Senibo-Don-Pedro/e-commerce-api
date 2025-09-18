package com.senibo.e_commerce_api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Standard success response format for API operations")
public record ApiSuccessResponse<T>(
        @Schema(description = "Indicates if the API call was successful. Always `true` for success.",
                example = "true")
        boolean success,

        @Schema(description = "Human-readable success message")
        String message,
        T data
) {
}
