package com.senibo.e_commerce_api.model.product;

import com.senibo.e_commerce_api.model.BaseEntity;
import com.senibo.e_commerce_api.model.cartItem.CartItem;
import com.senibo.e_commerce_api.model.orderItem.OrderItem;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer stockQuantity;

    @Column(nullable = false, length = 100)
    @Enumerated(EnumType.STRING)
    private ProductCategory category;

    private String imageUrl;

    @Column(unique = true, nullable = false)
    private String sku;

    //Relationships

    // One Product can be in MANY CartItems, so this must be a collection
    @OneToMany(mappedBy = "product") // Removed cascade, it's safer
    private Set<CartItem> cartItems; // <-- Change to Set and plural name

    // One Product can be in MANY OrderItems
    @OneToMany(mappedBy = "product") // Removed cascade
    private Set<OrderItem> orderItems; // <-- Change to Set and plural name

}
