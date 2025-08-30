package com.senibo.e_commerce_api.model.product;

import com.senibo.e_commerce_api.model.BaseEntity;
import com.senibo.e_commerce_api.model.cart.CartItem;
import com.senibo.e_commerce_api.model.category.Category;
import com.senibo.e_commerce_api.model.order.OrderItem;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products", indexes = {
    @Index(name = "idx_product_sku", columnList = "sku"),
    @Index(name = "idx_product_category", columnList = "category_id"),
    @Index(name = "idx_product_active", columnList = "is_active")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product extends BaseEntity {
    
    @Column(unique = true, nullable = false)
    private String sku; // Stock Keeping Unit
    
    @Column(nullable = false)
    private String name;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
    
    @Column(nullable = false)
    @Builder.Default
    private Integer stockQuantity = 0;
    
    @Column(nullable = false)
    @Builder.Default
    private Integer reservedQuantity = 0; // For items in carts but not yet purchased
    
    @Column
    private String imageUrl;
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean isActive = true;
    
    @Column(nullable = false)
    @Builder.Default
    private Double weight = 0.0; // For shipping calculations
    
    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @Builder.Default
    private List<CartItem> cartItems = new ArrayList<>();
    
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @Builder.Default
    private List<OrderItem> orderItems = new ArrayList<>();
    
    // Helper method for available stock
    public Integer getAvailableStock() {
        return stockQuantity - reservedQuantity;
    }
}
