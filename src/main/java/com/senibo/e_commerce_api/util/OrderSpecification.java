package com.senibo.e_commerce_api.util;

import com.senibo.e_commerce_api.model.auth.User;
import com.senibo.e_commerce_api.model.order.Order;
import com.senibo.e_commerce_api.model.order.OrderStatus;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderSpecification {

    /**
     * The main builder method that dynamically constructs the final specification.
     * It ALWAYS includes a filter for the user.
     */
    public static Specification<Order> build(
            User user, // This is a mandatory parameter
            Optional<OrderStatus> status
    ) {
        // Start with a list that always contains the user specification.
        List<Specification<Order>> specs = new ArrayList<>();
        specs.add(forUser(user));

        // Add other specifications only if the filter is present.
        status.ifPresent(s -> specs.add(hasStatus(s)));

        // Combine all specifications with an "AND" operation.
        return specs.stream().reduce(Specification::and).orElse(null);
    }

    // --- Private "Lego Brick" Methods ---

    /**
     * Creates a mandatory criteria: WHERE order.user = :user
     */
    private static Specification<Order> forUser(User user) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("user"), user);
    }

    /**
     * Creates an optional criteria: WHERE order.orderStatus = :status
     */
    private static Specification<Order> hasStatus(OrderStatus status) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("orderStatus"), status);
    }
}
