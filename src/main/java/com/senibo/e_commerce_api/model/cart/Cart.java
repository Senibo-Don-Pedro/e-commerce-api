package com.senibo.e_commerce_api.model.cart;

import com.senibo.e_commerce_api.model.BaseEntity;
import com.senibo.e_commerce_api.model.auth.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts", indexes = {
    @Index(name = "idx_cart_user", columnList = "user_id"),
    @Index(name = "idx_cart_updated", columnList = "updated_at")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"user", "items"})
public class Cart extends BaseEntity {
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
    
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CartItem> items = new ArrayList<>();
    
    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal totalAmount = BigDecimal.ZERO;
    
    @Column(name = "total_items", nullable = false)
    @Builder.Default
    private Integer totalItems = 0;
    
    @Column(name = "session_id") // For guest users (future enhancement)
    private String sessionId;
    
    // Helper methods
    public void recalculateTotal() {
        this.totalAmount = items.stream()
            .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        this.totalItems = items.stream()
            .mapToInt(CartItem::getQuantity)
            .sum();
    }
    
    public boolean isEmpty() {
        return items.isEmpty() || totalItems == 0;
    }
}
