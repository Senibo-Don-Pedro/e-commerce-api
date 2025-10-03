package com.senibo.e_commerce_api.dto.order;

import com.senibo.e_commerce_api.model.order.Order;
import com.senibo.e_commerce_api.model.order.OrderStatus;
import com.senibo.e_commerce_api.model.product.Product;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Schema(description = "Detailed information about a customer's order")
public record OrderDTO(
        UUID orderId,
        LocalDateTime orderDate,
        OrderStatus orderStatus,
        BigDecimal totalAmount,
        List<OrderItemDto> items
) {
    public static OrderDTO mapOrderToDto(Order order) {
        List<OrderDTO.OrderItemDto> itemDtos = order.getOrderItems().stream()
                                                    .map(orderItem -> {
                                                        Product product = orderItem.getProduct();
                                                        return new OrderDTO.OrderItemDto(
                                                                product.getId().toString(),
                                                                product.getName(),
                                                                product.getImageUrl(),
                                                                orderItem.getQuantity(),
                                                                orderItem.getPricePerUnit()
                                                        );
                                                    }).collect(Collectors.toList());

        return new OrderDTO(
                order.getId(),
                order.getCreatedAt(), // Assuming your BaseEntity has a createdAt field
                order.getOrderStatus(),
                order.getTotalAmount(),
                itemDtos
        );
    }

    @Schema(description = "Details of a single item within an order")
    private record OrderItemDto(
            String productId,
            String productName,
            String imageUrl, // It's good to include an image for the UI
            Integer quantity,
            BigDecimal pricePerUnit
    ) {
    }
}
