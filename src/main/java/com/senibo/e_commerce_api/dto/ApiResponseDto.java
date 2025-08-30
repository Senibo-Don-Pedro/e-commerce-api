package com.senibo.e_commerce_api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponseDto<Data>(
        Boolean success,
        String message,
        Data data,
        String error,
        List<String> errors
) {

    public static <Data> ApiResponseDto<Data> success(String message, Data data) {
        return new ApiResponseDto<>(true, message, data, null, null);
    }

    public static <Data> ApiResponseDto<Data> error(String error) {
        return new ApiResponseDto<>(false, null, null, error, null);
    }
    public static <Data> ApiResponseDto<Data> error(String error, List<String> errors) {
        return new ApiResponseDto<>(false, null, null, error, errors);
    }
    public static <Data> ApiResponseDto<Data> validationErrors(List<String> errors) {
        return new ApiResponseDto<>(false, null, null, "Validation failed", errors);
    }
}
