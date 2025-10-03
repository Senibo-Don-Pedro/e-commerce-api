package com.senibo.e_commerce_api.controller;

import com.senibo.e_commerce_api.dto.ApiSuccessResponse;
import com.senibo.e_commerce_api.dto.PagedResult;
import com.senibo.e_commerce_api.dto.order.OrderDTO;
import com.senibo.e_commerce_api.model.order.OrderStatus;
import com.senibo.e_commerce_api.security.services.UserDetailsImpl;
import com.senibo.e_commerce_api.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Tag(name = "order-controller", description = "Endpoints for managing customer orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    @Operation(summary = "Get the current user's order history with pagination and filtering",
            description = "Retrieves a paginated list of all past and present orders for the currently authenticated user. Requires a valid JWT.")
    public ResponseEntity<ApiSuccessResponse<PagedResult<OrderDTO>>> getUserOrderHistory(
            @AuthenticationPrincipal UserDetailsImpl userDetails,

            // --- THIS IS THE NEW SECTION ---
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0")
            int page,

            @Parameter(description = "Number of items per page (max 100)", example = "10")
            @RequestParam(defaultValue = "10")
            int pageSize,

            @Parameter(description = "Field to sort by",
                    schema = @Schema(allowableValues = {"createdAt", "totalAmount", "orderStatus"}))
            @RequestParam(defaultValue = "createdAt")
            String sortBy,

            @Parameter(description = "Sort direction",
                    schema = @Schema(allowableValues = {"asc", "desc"}))
            @RequestParam(defaultValue = "desc")
            String sortDirection,

            @Parameter(description = "Filter by order status")
            @RequestParam(required = false)
            OrderStatus status
            // -----------------------------
    ) {
        // We pass all the new parameters to our upgraded service method.
        ApiSuccessResponse<PagedResult<OrderDTO>> response = orderService.getOrdersForUser(
                userDetails.getId(),
                page,
                pageSize,
                sortBy,
                sortDirection,
                Optional.ofNullable(status)
        );

        return ResponseEntity.ok(response);
    }
}
