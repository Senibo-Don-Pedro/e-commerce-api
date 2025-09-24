package com.senibo.e_commerce_api.model.order;

import com.senibo.e_commerce_api.model.BaseEntity;
import com.senibo.e_commerce_api.model.auth.User;
import com.senibo.e_commerce_api.model.orderItem.OrderItem;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order extends BaseEntity {

    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 100)
    @Builder.Default
    private OrderStatus orderStatus = OrderStatus.PENDING;

    private String paymentReference;

    // MANY Orders can belong to ONE User
    @ManyToOne // <-- Change to ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();
}
