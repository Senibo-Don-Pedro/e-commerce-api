package com.senibo.e_commerce_api.model.order;

import com.senibo.e_commerce_api.model.BaseEntity;
import com.senibo.e_commerce_api.model.product.Product;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items", indexes = {
    @Index(name = "idx_orderitem_order", columnList = "order_id"),
    @Index(name = "idx_orderitem_product", columnList = "product_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"order", "product"})

public class OrderItem extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    
    // Product snapshot at time of order (important for historical records)
    @Column(name = "product_name", nullable = false)
    private String productName;
    
    @Column(name = "product_sku", nullable = false)
    private String productSku;
    
    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;
    
    @Column(nullable = false)
    private Integer quantity;
    
    @Column(name = "line_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal lineTotal; // unitPrice * quantity
    
    // Optional: Product attributes at time of order
    @Column(name = "product_weight")
    private Double productWeight;
    
    @Column(name = "product_image_url")
    private String productImageUrl;
    
    // Helper methods
    public void calculateLineTotal() {
        this.lineTotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
    
    public BigDecimal getLineTotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
